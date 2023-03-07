package ru.practicum.chart_private.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chart_private.service.PrivateCommentService;
import ru.practicum.dto.CommentDto;
import ru.practicum.dto.NewCommentDto;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/{userId}/comments")
public class PrivateCommentController {
    private final PrivateCommentService service;

    /**
     * Сохранить комментарий
     */
    @PostMapping("/{eventId}")
    public ResponseEntity<CommentDto> save(@PathVariable Long userId,
                                           @PathVariable Long eventId,
                                           @RequestBody @Valid NewCommentDto newCommentDto) {
        return new ResponseEntity<>(service.save(userId, eventId, newCommentDto), HttpStatus.OK);
    }

    /**
     * Редактировать комментарий
     */
    @PatchMapping("/{commentId}")
    public ResponseEntity<CommentDto> update(@PathVariable Long userId,
                                             @PathVariable Long commentId,
                                             @RequestBody @Valid NewCommentDto newCommentDto) {
        return ResponseEntity.ok(service.update(commentId, userId, newCommentDto));
    }

    /**
     * Удалить комментарий
     */
    @DeleteMapping("/{commentId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId,
                                       @PathVariable Long commentId) {
        service.delete(commentId, userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
