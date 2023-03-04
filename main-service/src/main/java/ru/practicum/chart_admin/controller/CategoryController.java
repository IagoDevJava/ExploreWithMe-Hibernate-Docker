package ru.practicum.chart_admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chart_admin.service.AdminCategoryService;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/categories")
public class CategoryController {
    private final AdminCategoryService service;

    /**
     * Добавление новой категории
     */
    @PostMapping()
    public ResponseEntity<CategoryDto> save(@RequestBody @Valid NewCategoryDto newCategoryDto) {
        return new ResponseEntity<>(service.save(newCategoryDto), HttpStatus.CREATED);
    }

    /**
     * Удаление категории
     */
    @DeleteMapping("/{catId}")
    public ResponseEntity<Void> delete(@PathVariable Long catId) {
        service.delete(catId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Изменение категории
     */
    @PatchMapping("/{catId}")
    public ResponseEntity<CategoryDto> update(@RequestBody NewCategoryDto newCategoryDto,
                                              @PathVariable Long catId) {
        return ResponseEntity.ok(service.update(newCategoryDto, catId));
    }
}
