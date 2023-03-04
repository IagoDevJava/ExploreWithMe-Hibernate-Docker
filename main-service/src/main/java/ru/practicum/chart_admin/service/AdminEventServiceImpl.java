package ru.practicum.chart_admin.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequestDto;
import ru.practicum.enums.AdminState;
import ru.practicum.enums.State;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.DateTimeMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.model.Event;
import ru.practicum.model.QEvent;
import ru.practicum.repository.EventRepository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class AdminEventServiceImpl implements AdminEventService {
    private final EventRepository repository;
    private final AdminCategoryServiceImpl categoryService;

    /**
     * Поиск события
     */
    @Override
    public List<EventFullDto> getAllByAdminRequest(List<Long> users, List<State> states, List<Long> categories,
                                                   String rangeStart, String rangeEnd, Integer from, Integer size) {
        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        if (users != null && !users.isEmpty()) {
            conditions.add(event.initiator.id.in(users));
        }
        if (states != null && !states.isEmpty()) {
            conditions.add(event.state.in(states));
        }
        if (categories != null && !categories.isEmpty()) {
            conditions.add(event.category.id.in(categories));
        }
        if (rangeStart != null && !rangeStart.isEmpty()) {
            conditions.add(event.eventDate.after(DateTimeMapper.toLocalDateTime(rangeStart)));
        }
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            conditions.add(event.eventDate.before(DateTimeMapper.toLocalDateTime(rangeEnd)));
        }

        List<Event> events;
        if (conditions.isEmpty()) {
            events = repository.findAll();
        } else {
            BooleanExpression expression = conditions.stream()
                    .reduce(BooleanExpression::and)
                    .get();

            events = StreamSupport.stream(repository.findAll(expression).spliterator(), false)
                    .collect(Collectors.toList());
        }

        return EventMapper.toEventFullDtoList(events).stream().skip(from).limit(size).collect(Collectors.toList());
    }

    /**
     * Редактирование события
     */
    @Transactional
    @Override
    public EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        Event event = getByIdWithCheck(eventId);

        checkEventDateByAdmin(event, updateEventAdminRequestDto);

        setStateByAdmin(updateEventAdminRequestDto, event);

        updateEventByAdmin(updateEventAdminRequestDto, event);

        return EventMapper.toEventFullDto(repository.save(event));
    }

    private Event getByIdWithCheck(Long eventId) {
        return repository.findById(eventId)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }

    private void checkEventDateByAdmin(Event event, UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        if (updateEventAdminRequestDto.getEventDate() != null) {
            LocalDateTime eventDate = DateTimeMapper.toLocalDateTime(updateEventAdminRequestDto.getEventDate());
            LocalDateTime publishedOn = event.getPublishedOn();

            if (eventDate.isBefore(publishedOn.plusHours(1)) || eventDate.isBefore(LocalDateTime.now())) {
                throw new ForbiddenException(
                        "Field: eventDate. Error: должно содержать дату не ранее чем за час от даты публикации. Value: "
                                + eventDate);
            }
        }
    }

    private void setStateByAdmin(UpdateEventAdminRequestDto updateEventAdminRequestDto, Event event) {
        if (updateEventAdminRequestDto.getStateAction().equals(AdminState.PUBLISH_EVENT.toString())) {
            if (!event.getState().equals(State.PENDING)) {
                throw new ForbiddenException(
                        "Cannot publish the event because it's not in the right state: " + event.getState());
            }
            event.setState(State.PUBLISHED);
        }
        if (updateEventAdminRequestDto.getStateAction().equals(AdminState.REJECT_EVENT.toString())) {
            if (event.getState().equals(State.PUBLISHED)) {
                throw new ForbiddenException(
                        "Cannot reject the event because it's not in the right state: " + event.getState());
            }
            event.setState(State.CANCELED);
        }
    }

    private void updateEventByAdmin(UpdateEventAdminRequestDto updateEventAdminRequestDto, Event event) {
        if (updateEventAdminRequestDto.getAnnotation() != null) {
            event.setAnnotation(updateEventAdminRequestDto.getAnnotation());
        }
        if (updateEventAdminRequestDto.getCategory() != null) {
            event.setCategory(categoryService.getByIdWithCheck(updateEventAdminRequestDto.getCategory()));
        }
        if (updateEventAdminRequestDto.getDescription() != null) {
            event.setDescription(updateEventAdminRequestDto.getDescription());
        }
        if (updateEventAdminRequestDto.getEventDate() != null) {
            event.setEventDate(DateTimeMapper.toLocalDateTime(updateEventAdminRequestDto.getEventDate()));
        }
        if (updateEventAdminRequestDto.getLocation() != null) {
            event.setLocation(updateEventAdminRequestDto.getLocation());
        }
        if (updateEventAdminRequestDto.getPaid() != null) {
            event.setPaid(updateEventAdminRequestDto.getPaid());
        }
        if (updateEventAdminRequestDto.getParticipantLimit() != null) {
            event.setParticipantLimit(updateEventAdminRequestDto.getParticipantLimit());
        }
        if (updateEventAdminRequestDto.getRequestModeration() != null) {
            event.setRequestModeration(updateEventAdminRequestDto.getRequestModeration());
        }
        if (updateEventAdminRequestDto.getTitle() != null) {
            event.setTitle(updateEventAdminRequestDto.getTitle());
        }
    }
}
