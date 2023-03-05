package ru.practicum.chart_admin.service;

import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;

public interface AdminCategoryService {
    /**
     * Добавление новой категории
     */
    CategoryDto save(NewCategoryDto newCategoryDto);

    /**
     * Удаление категории
     */
    void delete(Long catId);

    /**
     * Изменение категории
     */
    CategoryDto update(NewCategoryDto newCategoryDto, Long catId);
}
