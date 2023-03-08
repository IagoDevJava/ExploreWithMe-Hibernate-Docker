package ru.practicum.chart_public.service;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CommentDto;
import ru.practicum.mapper.CommentMapper;
import ru.practicum.model.Comment;
import ru.practicum.repository.CommentRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PublicCommentServiceImpl implements PublicCommentService {
    CommentRepository repository;

    @Override
    public List<CommentDto> getCommentsByEventId(Long eventId) {
        List<Comment> allByEventId = repository.findAllByEventId(eventId);
        List<CommentDto> commentDtos = CommentMapper.toCommentDtoList(allByEventId);
        return commentDtos;
    }
}
