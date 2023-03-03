package ru.practicum.chartAdmin.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.chartAdmin.service.AdminUserService;
import ru.practicum.dto.NewRequestDto;
import ru.practicum.dto.UserDto;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/admin/users")
public class UserController {
    private final AdminUserService service;

    @Autowired
    public UserController(AdminUserService service) {
        this.service = service;
    }

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
