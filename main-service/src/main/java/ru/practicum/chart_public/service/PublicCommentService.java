package ru.practicum.chart_public.service;

import ru.practicum.dto.CommentDto;

import java.util.List;

public interface PublicCommentService {
    List<CommentDto> getCommentsByEventId(Long eventId);
}