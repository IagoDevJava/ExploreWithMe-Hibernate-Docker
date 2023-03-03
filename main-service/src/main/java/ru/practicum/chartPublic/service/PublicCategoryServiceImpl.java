package ru.practicum.chartPublic.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CategoryDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.CategoryMapper.toCategoryDto;
import static ru.practicum.mapper.CategoryMapper.toCategoryDtoList;

@Service
public class PublicCategoryServiceImpl implements PublicCategoryService {
    private final CategoryRepository repository;

    @Autowired
    public PublicCategoryServiceImpl(CategoryRepository repository) {
        this.repository = repository;
    }

    /**
     * Получение категорий
     */
    @Override
    public List<CategoryDto> getAll(Integer from, Integer size) {
        return toCategoryDtoList(repository.findAll().stream().skip(from).limit(size).collect(Collectors.toList()));
    }

    /**
     * Получение информации о категории по её идентификатору
     */
    @Override
    public CategoryDto getById(Long catId) {
        return toCategoryDto(getByIdWithCheck(catId));
    }

    private Category getByIdWithCheck(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }
}
