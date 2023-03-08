package ru.practicum.chart_public.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.chart_public.service.PublicCommentService;
import ru.practicum.dto.CommentDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/comments")
public class PublicCommentController {
    private final PublicCommentService service;

    @GetMapping("/{eventId}")
    public ResponseEntity<List<CommentDto>> getCommentsByEventId(@PathVariable Long eventId) {
        return ResponseEntity.ok(service.getCommentsByEventId(eventId));
    }
}
