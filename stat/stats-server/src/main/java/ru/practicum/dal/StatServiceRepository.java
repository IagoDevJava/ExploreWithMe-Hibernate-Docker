package ru.practicum.dal;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface StatServiceRepository extends JpaRepository<EndpointHit, Long> {
    /**
     * Формирование статистики всех запросов
     */
    List<EndpointHit> findEndpointHitByTimeAfterAndTimeBeforeAndUriEquals(
            LocalDateTime start, LocalDateTime end, String uri);

    /**
     * Формирование статистики уникальных запросов пор ip
     */
    @Query(value = "select distinct ip\n" +
            "from stats\n" +
            "where time > :start\n" +
            "  and time < :end\n" +
            "  and uri = :uri", nativeQuery = true)
    List<EndpointHit> findUniqueIp(
            @Param("start") LocalDateTime start, @Param("end") LocalDateTime end, @Param("uri") String uri);
}
