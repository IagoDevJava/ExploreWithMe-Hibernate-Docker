package ru.practicum.chart_admin.controller;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chart_admin.service.AdminUserService;
import ru.practicum.dto.NewRequestDto;
import ru.practicum.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/admin/users")
public class UserController {
    private final AdminUserService service;

    /**
     * Получение информации о пользователях
     */
    @GetMapping()
    public ResponseEntity<List<UserDto>> getAll(@RequestParam(required = false) List<Long> ids,
                                                @RequestParam(defaultValue = "0") Integer from,
                                                @RequestParam(defaultValue = "10") Integer size) {
        return new ResponseEntity<>(service.getAll(ids, from, size), HttpStatus.OK);
    }

    /**
     * Добавление нового пользователя
     */
    @PostMapping()
    public ResponseEntity<UserDto> save(@RequestBody @Valid NewRequestDto newRequestDto) {
        return new ResponseEntity<>(service.save(newRequestDto), HttpStatus.CREATED);
    }

    /**
     * Удаление пользователя
     */
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> delete(@PathVariable Long userId) {
        service.delete(userId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
