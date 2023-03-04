package ru.practicum.chart_public.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chart_public.service.PublicCompilationService;
import ru.practicum.dto.CompilationDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/compilations")
public class PublicCompilationController {
    private final PublicCompilationService service;

    /**
     * Получение подборок событий
     */
    @GetMapping()
    public ResponseEntity<List<CompilationDto>> getAll(@RequestParam(required = false) Boolean pinned,
                                                       @RequestParam(defaultValue = "0") Integer from,
                                                       @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAll(pinned, from, size));
    }

    /**
     * Получение подборки событий по его id
     */
    @GetMapping("/{compId}")
    public ResponseEntity<CompilationDto> getById(@PathVariable Long compId) {
        return ResponseEntity.ok(service.getById(compId));
    }
}
