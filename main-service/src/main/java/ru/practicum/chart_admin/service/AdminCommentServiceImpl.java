package ru.practicum.chart_admin.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CommentDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCommentServiceImpl implements AdminCommentService {
    CommentRepository repository;

    /**
     * Получить комментарий по id
     */
    @Override
    public CommentDto getById(Long commentId) {
        return CommentMapper.toCommentDto(checkComment(commentId));
    }

    /**
     * Удалить комментарий по id
     */
    @Transactional
    @Override
    public void delete(Long commentId) {
        checkComment(commentId);
        repository.deleteById(commentId);
    }

    private Comment checkComment(Long commentId) {
        return repository.findById(commentId).orElseThrow(
                () -> new NotFoundException(String.format("Comment with id=%d was not found", commentId)));
    }
}