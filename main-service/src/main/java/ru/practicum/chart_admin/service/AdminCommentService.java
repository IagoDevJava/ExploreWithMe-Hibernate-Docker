package ru.practicum.chart_admin.service;

import ru.practicum.dto.CommentDto;

public interface AdminCommentService {
    /**
     * Получить комментарий по id
     */
    CommentDto getById(Long commentId);

    /**
     * Удалить комментарий по id
     */
    void delete(Long commentId);
}
