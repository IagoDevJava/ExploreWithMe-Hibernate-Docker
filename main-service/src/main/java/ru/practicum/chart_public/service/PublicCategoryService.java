package ru.practicum.chart_public.service;

import ru.practicum.dto.CategoryDto;

import java.util.List;

public interface PublicCategoryService {
    /**
     * Получение категорий
     */
    List<CategoryDto> getAll(Integer from, Integer size);

    /**
     * Получение информации о категории по её идентификатору
     */
    CategoryDto getById(Long catId);
}