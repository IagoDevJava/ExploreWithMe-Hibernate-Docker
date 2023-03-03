package ru.practicum.chartAdmin.service;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.dto.CompilationDto;
import ru.practicum.dto.NewCompilationDto;
import ru.practicum.dto.UpdateCompilationRequestDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.mapper.CompilationMapper;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;
import ru.practicum.repository.EventRepository;

@Service
@Transactional(readOnly = true)
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class AdminCompilationServiceImpl implements AdminCompilationService {
    CompilationRepository repository;
    EventRepository eventRepository;

    @Autowired
    public AdminCompilationServiceImpl(CompilationRepository repository, EventRepository eventRepository) {
        this.repository = repository;
        this.eventRepository = eventRepository;
    }

    /**
     * Добавление новой подборки
     */
    @Transactional
    @Override
    public CompilationDto save(NewCompilationDto newCompilationDto) {
        Compilation compilation = CompilationMapper.toCompilation(newCompilationDto);
        compilation.setEvents(eventRepository.findAllByIdIn(newCompilationDto.getEvents()));

        return CompilationMapper.toCompilationDto(repository.save(compilation));
    }

    /**
     * Удаление подборки
     */
    @Transactional
    @Override
    public void delete(Long compId) {
        repository.findById(compId).orElseThrow(() -> new NotFoundException(
                String.format("Compilation with id=%d was not found", compId)));
        repository.deleteById(compId);
    }

    /**
     * Обновить информацию о подборке
     */
    @Transactional
    @Override
    public CompilationDto update(Long compId, UpdateCompilationRequestDto updateCompilationRequestDto) {
        Compilation compilation = getByIdWithCheck(compId);
        if (updateCompilationRequestDto.getTitle() != null) {
            compilation.setTitle(updateCompilationRequestDto.getTitle());
        }
        if (updateCompilationRequestDto.getPinned() != null) {
            compilation.setPinned(updateCompilationRequestDto.getPinned());
        }
        if (updateCompilationRequestDto.getEvents() != null) {
            compilation.setEvents(eventRepository.findAllByIdIn(updateCompilationRequestDto.getEvents()));
        }
        return CompilationMapper.toCompilationDto(repository.save(compilation));
    }

    private Compilation getByIdWithCheck(Long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
    }
}