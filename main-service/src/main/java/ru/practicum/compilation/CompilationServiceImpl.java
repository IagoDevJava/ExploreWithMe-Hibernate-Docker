package ru.practicum.compilation;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.compilation.dto.CompilationDto;
import ru.practicum.compilation.model.Compilation;
import ru.practicum.exception.NotFoundException;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.compilation.model.CompilationMapper.toCompilationDto;
import static ru.practicum.compilation.model.CompilationMapper.toCompilationDtoList;

@Service
public class CompilationServiceImpl implements CompilationService {
    private final CompilationRepository repository;

    @Autowired
    public CompilationServiceImpl(CompilationRepository repository) {
        this.repository = repository;
    }

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

    public Compilation getByIdWithCheck(Long compId) {
        return repository.findById(compId)
                .orElseThrow(() -> new NotFoundException(String.format("Compilation with id=%d was not found", compId)));
    }
}
