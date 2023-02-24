package ru.practicum.dal;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.practicum.dal.model.EndpointHit;
import ru.practicum.dal.model.EndpointHitMapper;
import ru.practicum.dto.EndpointHitDto;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        List<ViewStatsDto> result = new ArrayList<>();
        if (!unique) {
            for (String uri : uris) {
                List<EndpointHit> hitList = repository.findEndpointHitByTimeAfterAndTimeBeforeAndUriEquals(
                        LocalDateTime.parse(start), LocalDateTime.parse(end), uri);
                result.add(ViewStatsDto.builder().app(hitList.get(0).getApp()).uri(uri).hits(hitList.size()).build());
            }
        } else {
            for (String uri : uris) {
                List<EndpointHit> uniqueIp = repository.findUniqueIp(
                        LocalDateTime.parse(start), LocalDateTime.parse(end), uri);
                result.add(ViewStatsDto.builder().app(uniqueIp.get(0).getApp()).uri(uri).hits(uniqueIp.size()).build());
            }
        }
        return result;
    }
}
