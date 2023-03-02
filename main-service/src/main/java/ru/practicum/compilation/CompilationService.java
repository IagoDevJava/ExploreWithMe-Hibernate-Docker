package ru.practicum.compilation;

import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;

public interface CompilationService {
    /**
     * Получение подборок событий
     */
    List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size);

    /**
     * Получение подборки событий по его id
     */
    CompilationDto getById(Long compId);
}
