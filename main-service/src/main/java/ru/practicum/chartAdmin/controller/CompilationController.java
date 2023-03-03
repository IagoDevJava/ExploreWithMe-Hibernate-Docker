package ru.practicum.chartAdmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chartAdmin.service.AdminCompilationService;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequestDto;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/compilations")
public class CompilationController {
    private final AdminCompilationService service;

    @Autowired
    public CompilationController(AdminCompilationService service) {
        this.service = service;
    }

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
