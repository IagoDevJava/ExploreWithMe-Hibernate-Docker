package ru.practicum.chartPrivate.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chartPrivate.service.PrivateEventService;
import ru.practicum.dto.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/users/{userId}/events")
public class PrivateEventController {
    private final PrivateEventService service;

    @Autowired
    public PrivateEventController(PrivateEventService service) {
        this.service = service;
    }

    /**
     * Получение событий, добавленных текущим пользователем
     */
    @GetMapping("")
    public ResponseEntity<List<EventShortDto>> getAllByInitiatorId(@PathVariable Long userId,
                                                                   @RequestParam(defaultValue = "0") Integer from,
                                                                   @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAllByInitiatorId(userId, from, size));
    }

    /**
     * Добавление нового события
     */
    @PostMapping()
    public ResponseEntity<EventFullDto> save(@PathVariable Long userId,
                                             @RequestBody @Valid NewEventDto newEventDto) {
        return new ResponseEntity<>(service.save(userId, newEventDto), HttpStatus.CREATED);
    }

    /**
     * Получение полной информации о событии добавленном текущим пользователем
     */
    @GetMapping("{eventId}")
    public ResponseEntity<EventFullDto> getByIdAndInitiatorId(@PathVariable Long userId,
                                                              @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getByIdAndInitiatorId(eventId, userId), HttpStatus.OK);
    }

    /**
     * Изменение события добавленного текущим пользователем
     */
    @PatchMapping("{eventId}")
    public ResponseEntity<EventFullDto> updateByInitiator(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody @Valid UpdateEventUserRequestDto updateEventUserRequestDto) {
        return ResponseEntity.ok(service.updateByInitiator(eventId, userId, updateEventUserRequestDto));
    }

    /**
     * Получение информации о запросах на участие в событии текущего пользователя
     */
    @GetMapping("{eventId}/requests")
    public ResponseEntity<List<ParentRequestDto>> getRequestsByEventIdAndInitiatorId(@PathVariable Long userId,
                                                                                     @PathVariable Long eventId) {
        return new ResponseEntity<>(service.getRequestsByEventIdAndInitiatorId(eventId, userId), HttpStatus.OK);
    }

    /**
     * Изменение статуса (подтверждена, отменена) заявок на участие в событии текущего пользователя
     */
    @PatchMapping("{eventId}/requests")
    public ResponseEntity<StatusUpdateResult> updateRequestStatusByInitiator(
            @PathVariable Long userId,
            @PathVariable Long eventId,
            @RequestBody StatusUpdateRequest statusUpdateRequest) {
        return ResponseEntity.ok(service.updateRequestStatusByInitiator(eventId, userId, statusUpdateRequest));
    }
}