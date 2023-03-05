package ru.practicum.chart_public.service;

import com.querydsl.core.types.dsl.BooleanExpression;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;
import ru.practicum.model.enums.EventSort;
import ru.practicum.model.enums.State;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.DateTimeMapper;
import ru.practicum.mapper.EventMapper;
import ru.practicum.mapper.HitMapper;
import ru.practicum.model.Event;
import ru.practicum.model.QEvent;
import ru.practicum.repository.EventRepository;
import ru.practicum.stat.StatClient;

import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class PublicEventServiceImpl implements PublicEventService {
    private final EventRepository repository;
    private final StatClient statClient;

    /**
     * Получение событий с возможностью фильтрации
     */
    @Override
    public List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, String rangeStart,
                                      String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                                      Integer size, HttpServletRequest request) {
        statClient.addHit(HitMapper.toEndpointHitDto(request));

        Sort sortBy = Sort.unsorted();
        if (sort != null) {
            if (sort.toUpperCase().equals(EventSort.EVENT_DATE.toString())) {
                sortBy = Sort.by("eventDate");
            } else if (sort.toUpperCase().equals(EventSort.VIEWS.toString())) {
                sortBy = Sort.by("views");
            } else {
                throw new BadRequestException("Field: sort. Error: must be EVENT_DATE or VIEWS. Value: " + sort);
            }
        }

        QEvent event = QEvent.event;
        List<BooleanExpression> conditions = new ArrayList<>();

        conditions.add(event.state.eq(State.PUBLISHED));
        if (text != null && !text.isEmpty()) {
            conditions.add(event.annotation.toLowerCase().like('%' + text.toLowerCase() + '%')
                    .or(event.description.toLowerCase().like('%' + text.toLowerCase() + '%')));
        }
        if (categories != null && !categories.isEmpty()) {
            conditions.add(event.category.id.in(categories));
        }
        if (paid != null) {
            conditions.add(event.paid.eq(paid));
        }
        if (rangeStart != null && !rangeStart.isEmpty()) {
            conditions.add(event.eventDate.after(DateTimeMapper.toLocalDateTime(rangeStart)));
        }
        if (rangeEnd != null && !rangeEnd.isEmpty()) {
            conditions.add(event.eventDate.before(DateTimeMapper.toLocalDateTime(rangeEnd)));
        }
        if ((rangeStart == null || rangeStart.isEmpty()) && (rangeEnd == null || rangeEnd.isEmpty())) {
            conditions.add(event.eventDate.after(LocalDateTime.now()));
        }
        if (onlyAvailable == Boolean.TRUE) {
            conditions.add(event.participantLimit.gt(event.confirmedRequests));
        }

        BooleanExpression expression = conditions.stream()
                .reduce(BooleanExpression::and)
                .orElseThrow(() -> new NotFoundException("expression not found"));

        List<Event> events = StreamSupport.stream(repository.findAll(expression, sortBy).spliterator(), false)
                .skip(from).limit(size)
                .collect(Collectors.toList());

        return EventMapper.toEventShortDtoList(events);
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     */
    @Transactional
    @Override
    public EventFullDto getById(Long id, HttpServletRequest request) {
        statClient.addHit(HitMapper.toEndpointHitDto(request));

        Event event = getByIdWithCheck(id);

        if (!State.PUBLISHED.equals(event.getState())) {
            throw new ForbiddenException(String.format("Event with id=%d is not published", id));
        }

        event.setViews(event.getViews() + 1);
        repository.save(event);

        return EventMapper.toEventFullDto(event);
    }

    private Event getByIdWithCheck(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException(String.format("Event with id=%d was not found", id)));
    }
}
