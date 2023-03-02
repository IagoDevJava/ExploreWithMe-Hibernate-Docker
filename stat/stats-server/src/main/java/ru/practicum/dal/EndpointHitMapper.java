package ru.practicum.dal;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.EndpointHitDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class EndpointHitMapper {
    /**
     * Конвертация EndpointHit в EndpointHitDto
     */
    public static EndpointHitDto toEndpointHitDto(EndpointHit hit) {
        return EndpointHitDto.builder()
                .id(hit.getId())
                .app(hit.getApp())
                .uri(hit.getUri())
                .ip(hit.getIp())
                .time(hit.getTime().withNano(0))
                .build();
    }

    /**
     * Конвертация EndpointHitDto в EndpointHit
     */
    public static EndpointHit toEndpointHit(EndpointHitDto hitDto) {
        return EndpointHit.builder()
                .id(hitDto.getId())
                .app(hitDto.getApp())
                .uri(hitDto.getUri())
                .ip(hitDto.getIp())
                .time(hitDto.getTime().withNano(0))
                .build();
    }
}
