package ru.practicum.chart_private.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.*;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.Status;
import ru.practicum.model.enums.UserState;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.DateTimeMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Category;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivateEventServiceImpl implements PrivateEventService {
    EventRepository repository;
    CategoryRepository categoryRepository;
    UserRepository userRepository;
    RequestRepository requestRepository;

    /**
     * Получение событий, добавленных текущим пользователем
     */
    @Override
    public List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size) {
        return EventMapper.toEventShortDtoList(repository.findAllByInitiatorId(userId)
                .stream().skip(from).limit(size).collect(Collectors.toList()));
    }

    /**
     * Добавление нового события
     */
    @Transactional
    @Override
    public EventFullDto save(Long userId, NewEventDto newEventDto) {
        checkEventDateByInitiator(newEventDto.getEventDate());
        if (newEventDto.getAnnotation() == null) {
            throw new BadRequestException("Field: annotation. Error: must not be blank. Value: null");
        }
        Event event = EventMapper.toEvent(newEventDto);
        event.setCategory(categoryRepository.findById(newEventDto.getCategory())
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", newEventDto.getCategory()))));
        event.setConfirmedRequests(0L);
        event.setInitiator(userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId))));
        event.setViews(0L);
        return EventMapper.toEventFullDto(repository.save(event));
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     */
    @Override
    public EventFullDto getByIdAndInitiatorId(Long eventId, Long userId) {
        return EventMapper.toEventFullDto(getByIdAndInitiatorIdWithCheck(eventId, userId));
    }

    /**
     * Изменение события добавленного текущим пользователем
     */
    @Transactional
    @Override
    public EventFullDto updateByInitiator(Long eventId, Long userId, UpdateEventUserRequestDto updateEventUserRequestDto) {
        checkEventDateByInitiator(updateEventUserRequestDto.getEventDate());

        Event event = getByIdAndInitiatorIdWithCheck(eventId, userId);

        if (State.PUBLISHED.equals(event.getState())) {
            throw new ForbiddenException("Event must not be published");
        }

        setStateByInitiator(updateEventUserRequestDto, event);

        updateEventByInitiator(updateEventUserRequestDto, event);

        return EventMapper.toEventFullDto(repository.save(event));
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     */
    @Override
    public List<ParentRequestDto> getRequestsByEventIdAndInitiatorId(Long eventId, Long userId) {
        getByIdAndInitiatorIdWithCheck(eventId, userId);
        return RequestMapper.toParentRequestDtoList(requestRepository.findAllByEventId(eventId));
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     */
    @Transactional
    @Override
    public StatusUpdateResult updateRequestStatusByInitiator(
            Long eventId, Long userId, StatusUpdateRequest eventRequestStatusUpdateRequest) {
        List<ParentRequestDto> confirmedRequests = List.of();
        List<ParentRequestDto> rejectedRequests = List.of();

        List<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
        List<Request> requests = requestIds.stream()
                .map(this::getRequestByIdWithCheck)
                .collect(Collectors.toList());

        String status = eventRequestStatusUpdateRequest.getStatus();

        if (status.equals(Status.REJECTED.toString())) {
            rejectedRequests = requests.stream()
                    .peek(request -> request.setStatus(Status.REJECTED))
                    .map(requestRepository::save)
                    .map(RequestMapper::toParentRequestDto)
                    .collect(Collectors.toList());
            return new StatusUpdateResult(confirmedRequests, rejectedRequests);
        }

        Event event = getByIdIdWithCheck(eventId);
        Long participantLimit = event.getParticipantLimit();
        Long approvedRequests = event.getConfirmedRequests();
        Long availableParticipants = participantLimit - approvedRequests;
        Long potentialParticipants = (long) requestIds.size();

        if (participantLimit > 0 && participantLimit.equals(approvedRequests)) {
            throw new ConflictException(String.format("Event with id=%d has reached participant limit", eventId));
        }

        if (status.equals(Status.CONFIRMED.toString())) {
            if (participantLimit.equals(0L) || (potentialParticipants <= availableParticipants && !event.getRequestModeration())) {
                confirmedRequests = requests.stream()
                        .peek(request -> request.setStatus(Status.CONFIRMED))
                        .map(requestRepository::save)
                        .map(RequestMapper::toParentRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(approvedRequests + potentialParticipants);
            } else {
                confirmedRequests = requests.stream()
                        .limit(availableParticipants)
                        .peek(request -> request.setStatus(Status.CONFIRMED))
                        .map(requestRepository::save)
                        .map(RequestMapper::toParentRequestDto)
                        .collect(Collectors.toList());
                rejectedRequests = requests.stream()
                        .skip(availableParticipants)
                        .peek(request -> request.setStatus(Status.REJECTED))
                        .map(requestRepository::save)
                        .map(RequestMapper::toParentRequestDto)
                        .collect(Collectors.toList());
                event.setConfirmedRequests(participantLimit);
            }
        }
        repository.save(event);
        return new StatusUpdateResult(confirmedRequests, rejectedRequests);
    }

    private void checkEventDateByInitiator(String eventDate) {
        if (eventDate != null) {
            LocalDateTime dateTime = DateTimeMapper.toLocalDateTime(eventDate);
            if (dateTime.isBefore(LocalDateTime.now().plusHours(2))) {
                throw new ForbiddenException(
                        "Field: eventDate. Error: должно содержать дату, которая еще не наступила. Value: " + dateTime);
            }
        }
    }

    private void updateEventByInitiator(UpdateEventUserRequestDto updateEventUserRequestDto, Event event) {
        if (updateEventUserRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventUserRequestDto.getAnnotation());
        }
        if (updateEventUserRequestDto.getCategory() != null) {
            event.setCategory(getCategoryByIdWithCheck(updateEventUserRequestDto.getCategory()));
        }
        if (updateEventUserRequestDto.getDescription() != null) {
            event.setDescription(updateEventUserRequestDto.getDescription());
        }
        if (updateEventUserRequestDto.getEventDate() != null) {
            event.setEventDate(DateTimeMapper.toLocalDateTime(updateEventUserRequestDto.getEventDate()));
        }
        if (updateEventUserRequestDto.getLocation() != null) {
            event.setLocation(updateEventUserRequestDto.getLocation());
        }
        if (updateEventUserRequestDto.getPaid() != null) {
            event.setPaid(updateEventUserRequestDto.getPaid());
        }
        if (updateEventUserRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventUserRequestDto.getParticipantLimit());
        }
        if (updateEventUserRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventUserRequestDto.getRequestModeration());
        }
        if (updateEventUserRequestDto.getTitle() != null) {
            event.setTitle(updateEventUserRequestDto.getTitle());
        }
    }

    private void setStateByInitiator(UpdateEventUserRequestDto updateEventUserRequestDto, Event event) {
        if (!event.getState().equals(State.PENDING) && !event.getState().equals(State.CANCELED)) {
            throw new ForbiddenException("Only pending or canceled events can be changed");
        }
        if (updateEventUserRequestDto.getStateAction().equals(UserState.CANCEL_REVIEW.toString())) {
            event.setState(State.CANCELED);
        } else if (updateEventUserRequestDto.getStateAction().equals(UserState.SEND_TO_REVIEW.toString())) {
            event.setState(State.PENDING);
        } else {
            throw new ForbiddenException("Field: stateAction. Error: must be CANCEL_REVIEW or SEND_TO_REVIEW. Value: "
                    + updateEventUserRequestDto.getStateAction());
        }
    }

    private Category getCategoryByIdWithCheck(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }

    private Event getByIdAndInitiatorIdWithCheck(Long eventId, Long initiatorId) {
        return repository.findByIdAndInitiatorId(eventId, initiatorId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Event with id=%d and initiatorId=%d was not found", eventId, initiatorId)));
    }

    private Event getByIdIdWithCheck(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private Request getRequestByIdWithCheck(Long requestId) {
        Request request = requestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException(String.format("Request with id=%d was not found", requestId)));
        if (!request.getStatus().equals(Status.PENDING)) {
            throw new ConflictException("Request must have status pending");
        }
        return request;
    }
}