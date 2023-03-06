package ru.practicum.chart_private.service;

import ru.practicum.dto.CommentDto;
import ru.practicum.dto.NewCommentDto;

import java.util.List;

public interface PrivateCommentService {
//    List<CommentDto> getAllByAuthorId(Long userId);
    /**
     * Сохранить комментарий
     */
    CommentDto save(Long userId, Long eventId, NewCommentDto newCommentDto);

    /**
     * Редактировать комментарий
     */
    void delete(Long commentId, Long userId);

    /**
     * Удалить комментарий
     */
    CommentDto update(Long commentId, Long userId, NewCommentDto newCommentDto);
}
