package ru.practicum.chart_public.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chart_public.service.PublicCategoryService;
import ru.practicum.dto.CategoryDto;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/categories")
public class PublicCategoryController {
    private final PublicCategoryService service;

    /**
     * Получение категорий
     */
    @GetMapping()
    public ResponseEntity<List<CategoryDto>> getAll(@RequestParam(defaultValue = "0") Integer from,
                                                    @RequestParam(defaultValue = "10") Integer size) {
        return ResponseEntity.ok(service.getAll(from, size));
    }

    /**
     * Получение информации о категории по её идентификатору
     */
    @GetMapping("/{catId}")
    public ResponseEntity<CategoryDto> getById(@PathVariable Long catId) {
        return ResponseEntity.ok(service.getById(catId));
    }
}