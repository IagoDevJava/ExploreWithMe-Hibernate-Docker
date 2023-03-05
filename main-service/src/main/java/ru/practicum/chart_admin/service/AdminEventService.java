package ru.practicum.chart_admin.service;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequestDto;
import ru.practicum.model.enums.State;

import java.util.List;

public interface AdminEventService {
    /**
     * Поиск события
     */
    List<EventFullDto> getAllByAdminRequest(List<Long> users, List<State> states, List<Long> categories,
                                            String rangeStart, String rangeEnd, Integer from, Integer size);

    /**
     * Редактирование события
     */
    EventFullDto updateByAdmin(Long eventId, UpdateEventAdminRequestDto updateEventAdminRequestDto);
}
