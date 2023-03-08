package ru.practicum.chart_private.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.NewCommentDto;
import ru.practicum.enums.State;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.model.Event;
import ru.practicum.model.User;
import ru.practicum.repository.CommentRepository;
import ru.practicum.repository.EventRepository;
import ru.practicum.repository.UserRepository;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PrivateCommentServiceImpl implements PrivateCommentService {
    CommentRepository repository;
    UserRepository userRepository;
    EventRepository eventRepository;

    /**
     * Сохранить комментарий
     */
    @Transactional
    @Override
    public CommentDto save(Long userId, Long eventId, NewCommentDto newCommentDto) {
        User author = getUserByIdWithCheck(userId);
        Event event = getEventByIdWithCheck(eventId);

        if (!State.PUBLISHED.equals(event.getState())) {
            throw new BadRequestException("Event must be published");
        }

        Comment comment = CommentMapper.toComment(newCommentDto);
        comment.setEvent(event);
        comment.setAuthor(author);
        comment.setCreated(LocalDateTime.now());

        return CommentMapper.toCommentDto(repository.save(comment));
    }

    /**
     * Редактировать комментарий
     */
    @Transactional
    @Override
    public CommentDto update(Long commentId, Long userId, NewCommentDto newCommentDto) {
        Comment comment = getCommentByIdAndAuthorIdWithCheck(commentId, userId);

        if (comment.getCreated().plusDays(1).isBefore(LocalDateTime.now())) {
            throw new ForbiddenException("The comment update time has expired");
        }

        comment.setText(newCommentDto.getText());

        return CommentMapper.toCommentDto(repository.save(comment));
    }

    /**
     * Удалить комментарий
     */
    @Transactional
    @Override
    public void delete(Long commentId, Long userId) {
        getCommentByIdAndAuthorIdWithCheck(commentId, userId);
        repository.deleteById(commentId);
    }

    private Comment getCommentByIdAndAuthorIdWithCheck(Long commentId, Long userId) {
        return repository.findByIdAndAuthorId(commentId, userId).orElseThrow(
                () -> new NotFoundException(
                        String.format("Comment with id=%d and authorId=%d was not found", commentId, userId)));
    }

    private User getUserByIdWithCheck(Long userId) {
        return userRepository.findById(userId).orElseThrow(
                () -> new NotFoundException(String.format("User with id=%d was not found", userId)));
    }

    private Event getEventByIdWithCheck(Long eventId) {
        return eventRepository.findById(eventId).orElseThrow(
                () -> new NotFoundException(String.format("Event with id=%d was not found", eventId)));
    }
}