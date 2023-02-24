package ru.practicum.dal;

import ru.practicum.dal.model.EndpointHit;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.util.List;

public interface StatService {
    /**
     * Добавление статистики в БД
     */
    EndpointHitDto addHit(EndpointHit request);

    /**
     * Получение статистики
     */
    List<ViewStatsDto> getViewStats(String start, String end, List<String> uris, Boolean unique);
}
