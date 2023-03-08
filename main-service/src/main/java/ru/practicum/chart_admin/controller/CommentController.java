package ru.practicum.chart_admin.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chart_admin.service.AdminCommentService;
import ru.practicum.dto.CommentDto;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/comments")
public class CommentController {
    private final AdminCommentService service;

    /**
     * Получить комментарий по id
     */
    @GetMapping("/{commentId}")
    public ResponseEntity<CommentDto> getById(@PathVariable Long commentId) {
        return ResponseEntity.ok(service.getById(commentId));
    }

    /**
     * Удалить комментарий по id
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long commentId) {
        service.delete(commentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}