package ru.practicum.chart_public.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.dto.CompilationDto;
import ru.practicum.exception.NotFoundException;
import ru.practicum.model.Compilation;
import ru.practicum.repository.CompilationRepository;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.CompilationMapper.toCompilationDto;
import static ru.practicum.mapper.CompilationMapper.toCompilationDtoList;

@Service
@AllArgsConstructor
public class PublicCompilationServiceImpl implements PublicCompilationService {
    private final CompilationRepository repository;

    /**
     * Получение подборок событий
     */
    @Override
    public List<CompilationDto> getAll(Boolean pinned, Integer from, Integer size) {
        List<Compilation> compilations;
        if (pinned != null) {
            compilations = repository.findAllByPinned(pinned);
        } else {
            compilations = repository.findAll();
        }
        return toCompilationDtoList(compilations.stream().skip(from).limit(size).collect(Collectors.toList()));
    }

    /**
     * Получение подборки событий по его id
     */
    @Override
    public CompilationDto getById(Long compId) {
        return toCompilationDto(getByIdWithCheck(compId));
    }

    private Compilation getByIdWithCheck(Long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
    }
}
