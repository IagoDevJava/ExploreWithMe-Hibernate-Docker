package ru.practicum.dto;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ViewStatsDto {
    private String app;
    private String uri;
    private Integer hits;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ViewStatsDto that = (ViewStatsDto) o;
        return Objects.equals(app, that.app)
                && Objects.equals(uri, that.uri)
                && Objects.equals(hits, that.hits);
    }

    @Override
    public int hashCode() {
        return Objects.hash(app, uri, hits);
    }
}
