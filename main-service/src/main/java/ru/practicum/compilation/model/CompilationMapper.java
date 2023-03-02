package ru.practicum.compilation.model;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.compilation.dto.CompilationDto;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.event.model.EventMapper.toEventShortDtoList;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CompilationMapper {
    //    public static Compilation toCompilation(NewCompilationDto newCompilationDto) {
//        Compilation compilation = new Compilation();
//        compilation.setTitle(newCompilationDto.getTitle());
//        compilation.setPinned(newCompilationDto.getPinned());
//        return compilation;
//    }
//
    public static CompilationDto toCompilationDto(Compilation compilation) {
        return new CompilationDto(
                compilation.getId(),
                toEventShortDtoList(compilation.getEvents()),
                compilation.getPinned(),
                compilation.getTitle());
    }

    public static List<CompilationDto> toCompilationDtoList(List<Compilation> compilations) {
        return compilations.stream().map(CompilationMapper::toCompilationDto).collect(Collectors.toList());
    }
}
