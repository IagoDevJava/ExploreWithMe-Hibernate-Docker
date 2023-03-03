package ru.practicum.chartPrivate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chartPrivate.service.PrivateRequestService;
import ru.practicum.dto.ParentRequestDto;

import java.util.List;

@RestController
@RequestMapping("/users/{userId}/requests")
public class PrivateRequestController {
    private final PrivateRequestService service;

    @Autowired
    public PrivateRequestController(PrivateRequestService service) {
        this.service = service;
    }

    /**
     * Получение информации о заявках текущего пользователя на участие в чужих событиях
     */
    @GetMapping()
    public ResponseEntity<List<ParentRequestDto>> getAllByUserId(@PathVariable Long userId) {
        return ResponseEntity.ok(service.getAllByUserId(userId));
    }

    /**
     * Добавление запроса от текущего пользователя на участие в событии
     */
    @PostMapping()
    public ResponseEntity<ParentRequestDto> sendRequest(@PathVariable Long userId,
                                                        @RequestParam Long eventId) {
        return new ResponseEntity<>(service.sendRequest(userId, eventId), HttpStatus.CREATED);
    }

    /**
     * Отмена своего запроса на участие в событии
     */
    @PatchMapping("/{requestId}/cancel")
    public ResponseEntity<ParentRequestDto> cancelRequest(@PathVariable Long userId,
                                                          @PathVariable Long requestId) {
        return ResponseEntity.ok(service.cancelRequest(userId, requestId));
    }

}
