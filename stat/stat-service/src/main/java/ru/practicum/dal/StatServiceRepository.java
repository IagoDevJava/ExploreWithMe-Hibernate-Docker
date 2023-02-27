package ru.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.dal.model.EndpointHit;
import ru.practicum.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatServiceRepository extends JpaRepository<EndpointHit, Long> {

    /**
     * Формирование статистики всех запросов
     */
    @Query(value = "SELECT s.app, s.uri, COUNT(s.ip) " +
            "FROM stats s " +
            "WHERE s.time BETWEEN ?1 AND ?2 " +
            "AND s.uri IN (?3) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(s.ip) DESC", nativeQuery = true)
    List<ViewStatsDto> findAllViewStats(LocalDateTime start, LocalDateTime end, List<String> uris);

    /**
     * Формирование статистики уникальных запросов пор ip
     */
    @Query(value = "SELECT s.app, s.uri, COUNT(DISTINCT s.ip) " +
            "FROM stats s " +
            "WHERE s.time BETWEEN ?1 AND ?2 " +
            "AND s.uri IN (?3) " +
            "GROUP BY s.app, s.uri " +
            "ORDER BY COUNT(DISTINCT s.ip) DESC", nativeQuery = true)
    List<ViewStatsDto> findUniqueViewStat(LocalDateTime start, LocalDateTime end, List<String> uris);
}
