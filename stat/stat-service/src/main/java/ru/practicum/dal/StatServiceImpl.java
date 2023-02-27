package ru.practicum.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dal.model.EndpointHit;
import ru.practicum.dal.model.EndpointHitMapper;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Slf4j
@Service
public class StatServiceImpl implements StatService {
    private final StatServiceRepository repository;

    @Autowired
    public StatServiceImpl(StatServiceRepository repository) {
        this.repository = repository;
    }

    /**
     * Добавление статистики в БД
     */
    @Override
    public EndpointHitDto addHit(EndpointHit hit) {
        repository.save(hit);
        return EndpointHitMapper.toEndpointHitDto(hit);
    }

    /**
     * Получение статистики
     */
    @Override
    public List<ViewStatsDto> getViewStats(String start, String end, List<String> uris, Boolean unique) {
        if (!unique) {
            return repository.findAllViewStats(getDateTime(start), getDateTime(end), uris);
        } else {
            return repository.findUniqueViewStat(getDateTime(start), getDateTime(end), uris);
        }
    }

    private static LocalDateTime getDateTime(String dateTime) {
        dateTime = URLDecoder.decode(dateTime, StandardCharsets.UTF_8);
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
