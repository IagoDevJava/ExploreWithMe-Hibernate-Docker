package ru.practicum.chart_admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chart_admin.service.AdminCompilationService;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequestDto;

import javax.validation.Valid;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/compilations")
public class CompilationController {
    private final AdminCompilationService service;

    /**
     * Добавление новой подборки
     */
    @PostMapping()
    public ResponseEntity<CompilationDto> save(@RequestBody @Valid NewCompilationDto newCompilationDto) {
        return new ResponseEntity<>(service.save(newCompilationDto), HttpStatus.CREATED);
    }

    /**
     * Удаление подборки
     */
    @DeleteMapping("/{compId}")
    public ResponseEntity<Void> delete(@PathVariable Long compId) {
        service.delete(compId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    /**
     * Обновить информацию о подборке
     */
    @PatchMapping("/{compId}")
    public ResponseEntity<CompilationDto> update(@PathVariable Long compId,
                                                 @RequestBody UpdateCompilationRequestDto updateCompilationRequestDto) {
        return ResponseEntity.ok(service.update(compId, updateCompilationRequestDto));
    }
}
