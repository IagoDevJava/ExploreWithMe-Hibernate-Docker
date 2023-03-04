package ru.practicum.chart_admin.service;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CategoryDto;
import ru.practicum.dto.NewCategoryDto;
import ru.practicum.exception.BadRequestException;
import ru.practicum.exception.ConflictException;
import ru.practicum.exception.ForbiddenException;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CategoryMapper;
import ru.practicum.model.Category;
import ru.practicum.repository.CategoryRepository;
import ru.practicum.repository.EventRepository;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCategoryServiceImpl implements AdminCategoryService {
    CategoryRepository repository;
    EventRepository eventRepository;

    /**
     * Добавление новой категории
     */
    @Transactional
    @Override
    public CategoryDto save(NewCategoryDto newCategoryDto) {
        for (Category category : repository.findAll()) {
            if (category.getName().equals(newCategoryDto.getName())) {
                throw new ConflictException("This cat already use");
            }
        }
        return CategoryMapper.toCategoryDto(repository.save(CategoryMapper.toCategory(newCategoryDto)));
    }

    /**
     * Удаление категории
     */
    @Transactional
    @Override
    public void delete(Long catId) {
        getByIdWithCheck(catId);
        if (eventRepository.findAllByCategoryId(catId).size() == 0) {
            repository.deleteById(catId);
        } else {
            throw new ForbiddenException("The category is not empty");
        }
    }

    /**
     * Изменение категории
     */
    @Transactional
    @Override
    public CategoryDto update(NewCategoryDto newCategoryDto, Long catId) {
        if (newCategoryDto.getName() == null) {
            throw new BadRequestException("Field: name. Error: must not be blank. Value: null");
        }
        for (Category category : repository.findAll()) {
            if (category.getName().equals(newCategoryDto.getName())) {
                throw new ConflictException("This cat already use");
            }
        }

        Category category = getByIdWithCheck(catId);
        category.setName(newCategoryDto.getName());
        return CategoryMapper.toCategoryDto(repository.save(category));
    }

    public Category getByIdWithCheck(Long catId) {
        return repository.findById(catId)
                .orElseThrow(() -> new NotFoundException(String.format("Category with id=%d was not found", catId)));
    }
}
