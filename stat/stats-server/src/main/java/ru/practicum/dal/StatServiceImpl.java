package ru.practicum.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
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
        if (hit.getTime() == null) {
            hit.setTime(LocalDateTime.now().withNano(0));
        }
        repository.save(hit);
        return EndpointHitMapper.toEndpointHitDto(hit);
    }

    /**
     * Получение статистики
     */
    @Override
    public List<ViewStatsDto> getViewStats(String start, String end, List<String> uris, Boolean unique) {
        List<ViewStatsDto> result = new ArrayList<>();
        if (!unique) {
            for (String uri : uris) {
                List<EndpointHit> hitList = repository.findEndpointHitByTimeAfterAndTimeBeforeAndUriEquals(
                        getDateTime(start), getDateTime(end), uri);
                result.add(ViewStatsDto.builder().app(hitList.get(0).getApp()).uri(uri).hits(hitList.size()).build());
            }
        } else {
            for (String uri : uris) {
                List<EndpointHit> uniqueIp = repository.findUniqueIp(
                        getDateTime(start), getDateTime(end), uri);
                result.add(ViewStatsDto.builder().app(uniqueIp.get(0).getApp()).uri(uri).hits(uniqueIp.size()).build());
            }
        }
        Collections.sort(result);
        return result;
    }

    private static LocalDateTime getDateTime(String dateTime) {
        dateTime = URLDecoder.decode(dateTime, StandardCharsets.UTF_8);
        return LocalDateTime.parse(dateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }
}
