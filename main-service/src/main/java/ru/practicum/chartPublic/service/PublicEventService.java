package ru.practicum.chartPublic.service;

import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface PublicEventService {
    /**
     * Получение событий с возможностью фильтрации
     */
    List<EventShortDto> getAll(String text, List<Long> categories, Boolean paid, String rangeStart,
                               String rangeEnd, Boolean onlyAvailable, String sort, Integer from,
                               Integer size, HttpServletRequest request);

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     */
    EventFullDto getById(Long id, HttpServletRequest request);
}
