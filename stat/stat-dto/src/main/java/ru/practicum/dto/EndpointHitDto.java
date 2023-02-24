package ru.practicum.dto;

import jdk.jfr.Timestamp;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EndpointHitDto {
    private long id;
    @NotBlank
    private String app;
    private String uri;
    @NotBlank
    @Pattern(regexp = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]\\.[0-9]")
    private String ip;
    @Timestamp
    private LocalDateTime time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointHitDto that = (EndpointHitDto) o;
        return id == that.id
                && Objects.equals(app, that.app)
                && Objects.equals(uri, that.uri)
                && Objects.equals(ip, that.ip)
                && Objects.equals(time, that.time);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, app, uri, ip, time);
    }
}
