package ru.practicum.chartAdmin.service;

import ru.practicum.dto.NewRequestDto;
import ru.practicum.dto.UserDto;

import java.util.List;

public interface AdminUserService {
    /**
     * Получение информации о пользователях
     */
    List<UserDto> getAll(List<Long> ids, Integer from, Integer size);

    /**
     * Добавление нового пользователя
     */
    UserDto save(NewRequestDto newUserRequest);

    /**
     * Удаление пользователя
     */
    void delete(Long userId);
}