package ru.practicum.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@Slf4j
@RestController
@Validated
public class StatServiceController {
    private final StatServiceImpl service;

    @Autowired
    public StatServiceController(StatServiceImpl service) {
        this.service = service;
    }

    /**
     * Добавление статистики в БД
     */
    @PostMapping("/hit")
    public ResponseEntity<EndpointHitDto> addHit(@Valid @RequestBody EndpointHit hit) {
        service.addHit(hit);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    /**
     * Получение статистики
     */
    @GetMapping("/stats")
    public ResponseEntity<List<ViewStatsDto>> getViewStats(@RequestParam String start,
                                                           @RequestParam String end,
                                                           @NotNull @RequestParam List<String> uris,
                                                           @RequestParam(defaultValue = "false") Boolean unique) {
        return ResponseEntity.ok(service.getViewStats(start, end, uris, unique));
    }
}