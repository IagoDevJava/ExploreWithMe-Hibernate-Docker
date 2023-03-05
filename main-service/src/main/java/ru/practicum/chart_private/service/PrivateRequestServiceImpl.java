package ru.practicum.chart_private.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.ParentRequestDto;
import ru.practicum.model.enums.State;
import ru.practicum.model.enums.Status;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.RequestMapper;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.RequestRepository;
import ru.practicum.repository.UserRepository;

import java.util.List;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivateRequestServiceImpl implements PrivateRequestService {
    RequestRepository repository;
    EventRepository eventRepository;
    UserRepository userRepository;

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     */
    @Override
    public List<ParentRequestDto> getAllByUserId(Long userId) {
        getUserByIdWithCheck(userId);
        return RequestMapper.toParentRequestDtoList(repository.findAllByRequesterId(userId));
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     */
    @Transactional
    @Override
    public ParentRequestDto sendRequest(Long userId, Long eventId) {
        Event event = getEventByIdWithCheck(eventId);
        User requester = getUserByIdWithCheck(userId);

        if (repository.findByRequesterIdAndEventId(userId, eventId).isPresent()) {
            throw new ForbiddenException(
                    String.format("Request with requesterId=%d and eventId=%d already exist", userId, eventId));
        }
        if (userId.equals(event.getInitiator().getId())) {
            throw new ConflictException(String.format("User with id=%d must not be equal to initiator", userId));
        }
        if (!event.getState().equals(State.PUBLISHED)) {
            throw new ConflictException(String.format("Event with id=%d is not published", eventId));
        }
        if (event.getParticipantLimit().equals(event.getConfirmedRequests())) {
            throw new ConflictException(String.format("Event with id=%d has reached participant limit", eventId));
        }
        if (!event.getRequestModeration()) {
            event.setConfirmedRequests(event.getConfirmedRequests() + 1);
            eventRepository.save(event);
        }

        return RequestMapper.toParentRequestDto(repository.save(RequestMapper.toRequest(event, requester)));
    }

    /**
     * Отмена своего запроса на участие в событии
     */
    @Transactional
    @Override
    public ParentRequestDto cancelRequest(Long userId, Long requestId) {
        Request request = getByIdAndRequesterIdWithCheck(requestId, userId);
        request.setStatus(Status.CANCELED);
        return RequestMapper.toParentRequestDto(repository.save(request));
    }

    private Event getEventByIdWithCheck(Long eventId) {
        return eventRepository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private User getUserByIdWithCheck(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Request getByIdAndRequesterIdWithCheck(Long requestId, Long userId) {
        return repository.findByIdAndRequesterId(requestId, userId)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Request with id=%d and requesterId=%d was not found", requestId, userId)));
    }
}
