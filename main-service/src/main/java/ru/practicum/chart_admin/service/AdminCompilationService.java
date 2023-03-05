package ru.practicum.chart_admin.service;

import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequestDto;

public interface AdminCompilationService {
    /**
     * Добавление новой подборки
     */
    CompilationDto save(NewCompilationDto newCompilationDto);

    /**
     * Удаление подборки
     */
    void delete(Long compId);

    /**
     * Обновить информацию о подборке
     */
    CompilationDto update(Long compId, UpdateCompilationRequestDto updateCompilationRequestDto);
}
