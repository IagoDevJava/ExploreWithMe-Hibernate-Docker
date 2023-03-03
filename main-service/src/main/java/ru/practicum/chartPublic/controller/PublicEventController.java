package ru.practicum.chartPublic.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chartPublic.service.PublicEventService;
import ru.practicum.dto.EventFullDto;
import ru.practicum.dto.EventShortDto;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


@RestController
@RequestMapping("/events")
public class PublicEventController {
    private final PublicEventService service;

    @Autowired
    public PublicEventController(PublicEventService service) {
        this.service = service;
    }

    /**
     * Получение событий с возможностью фильтрации
     */
    @GetMapping()
    public ResponseEntity<List<EventShortDto>> getAll(@RequestParam(required = false) String text,
                                                      @RequestParam(required = false) List<Long> categories,
                                                      @RequestParam(required = false) Boolean paid,
                                                      @RequestParam(required = false) String rangeStart,
                                                      @RequestParam(required = false) String rangeEnd,
                                                      @RequestParam(defaultValue = "FALSE") Boolean onlyAvailable,
                                                      @RequestParam(required = false) String sort,
                                                      @RequestParam(defaultValue = "0") Integer from,
                                                      @RequestParam(defaultValue = "10") Integer size,
                                                      HttpServletRequest request) {
        return ResponseEntity.ok(service.getAll(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort, from, size, request));
    }

    /**
     * Получение подробной информации об опубликованном событии по его идентификатору
     */
    @GetMapping("/{id}")
    public ResponseEntity<EventFullDto> getById(@PathVariable Long id, HttpServletRequest request) {
        return ResponseEntity.ok(service.getById(id, request));
    }
}
