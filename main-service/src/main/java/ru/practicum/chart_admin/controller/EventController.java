package ru.practicum.chart_admin.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chart_admin.service.AdminEventService;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.UpdateEventAdminRequestDto;
import ru.practicum.enums.State;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@Slf4j
@RestController
@AllArgsConstructor
@RequestMapping("/admin/events")
public class EventController {
    private final AdminEventService service;

    /**
     * Поиск события
     */
    @GetMapping()
    public ResponseEntity<List<EventFullDto>> getAllByAdminRequest(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<State> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @PositiveOrZero @RequestParam(defaultValue = "0") Integer from,
            @PositiveOrZero @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service
                .getAllByAdminRequest(users, states, categories, rangeStart, rangeEnd, from, size));
    }

    /**
     * Редактирование события
     */
    @PatchMapping("/{eventId}")
    public ResponseEntity<EventFullDto> updateByAdmin(
            @PathVariable Long eventId,
            @RequestBody UpdateEventAdminRequestDto updateEventAdminRequestDto) {
        return ResponseEntity.ok(service.updateByAdmin(eventId, updateEventAdminRequestDto));
    }
}
