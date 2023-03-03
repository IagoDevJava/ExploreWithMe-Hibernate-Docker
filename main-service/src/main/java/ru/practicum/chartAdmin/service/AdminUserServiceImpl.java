package ru.practicum.chartAdmin.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.NewRequestDto;
import ru.practicum.dto.UserDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.UserMapper.*;

@Service
@Transactional(readOnly = true)
public class AdminUserServiceImpl implements AdminUserService {
    private final UserRepository repository;

    @Autowired
    public AdminUserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    /**
     * Получение информации о пользователях
     */
    @Override
    public List<UserDto> getAll(List<Long> ids, Integer from, Integer size) {
        if (ids == null) {
            return toUserDtoList(repository.findAll().stream().skip(from).limit(size).collect(Collectors.toList()));
        } else {
            return toUserDtoList(repository.findAllById(ids));
        }
    }

    /**
     * Добавление нового пользователя
     */
    @Transactional
    @Override
    public UserDto save(NewRequestDto newRequestDto) {
        if (newRequestDto.getName() == null) {
            throw new BadRequestException("Name must not be blank.");
        }
        return toUserDto(repository.save(toUser(newRequestDto)));
    }

    /**
     * Удаление пользователя
     */
    @Transactional
    @Override
    public void delete(Long userId) {
        repository.findById(userId)
                .orElseThrow(() -> new NotFoundException(String.format("User with id=%d was not found", userId)));
        repository.deleteById(userId);
    }
}
