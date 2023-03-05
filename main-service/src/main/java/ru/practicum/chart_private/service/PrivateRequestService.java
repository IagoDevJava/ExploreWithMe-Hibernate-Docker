package ru.practicum.chart_private.service;

import ru.practicum.dto.ParentRequestDto;

import java.util.List;

public interface PrivateRequestService {
    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     */
    List<ParentRequestDto> getAllByUserId(Long userId);

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     */
    ParentRequestDto sendRequest(Long userId, Long eventId);

    /**
     * Отмена своего запроса на участие в событии
     */
    ParentRequestDto cancelRequest(Long userId, Long requestId);
}
