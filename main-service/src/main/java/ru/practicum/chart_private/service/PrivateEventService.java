package ru.practicum.chart_private.service;

import ru.practicum.dto.*;

import java.util.List;

public interface PrivateEventService {
    /**
     * Получение событий, добавленных текущим пользователем
     */
    List<EventShortDto> getAllByInitiatorId(Long userId, Integer from, Integer size);

    /**
     * Добавление нового события
     */
    EventFullDto save(Long userId, NewEventDto newEventDto);

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     */
    EventFullDto getByIdAndInitiatorId(Long eventId, Long userId);

    /**
     * Изменение события добавленного текущим пользователем
     */
    EventFullDto updateByInitiator(Long eventId, Long userId, UpdateEventUserRequestDto updateEventUserRequest);

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     */
    List<ParentRequestDto> getRequestsByEventIdAndInitiatorId(Long eventId, Long userId);

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     */
    StatusUpdateResult updateRequestStatusByInitiator(
            Long eventId, Long userId, StatusUpdateRequest eventRequestStatusUpdateRequest);
}