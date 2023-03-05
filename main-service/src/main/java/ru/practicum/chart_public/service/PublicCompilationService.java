package ru.practicum.chart_public.service;

import ru.practicum.dto.CompilationDto;

import java.util.List;

public interface PublicCompilationService {
    /**
     * Получение подборок событий
     */
    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    /**
     * Получение подборки событий по его id
     */
    CompilationDto getById(Long compId);
}
