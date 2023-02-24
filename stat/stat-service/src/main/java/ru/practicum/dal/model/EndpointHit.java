package ru.practicum.dal.model;

import jdk.jfr.Timestamp;
import lombok.*;
import org.hibernate.validator.constraints.URL;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.Objects;

@Entity
@Table(name = "stats")
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EndpointHit {
    /**
     * * Идентификатор записи
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private long id;

    /**
     * Идентификатор сервиса для которого записывается информация
     */
    @Column
    @NotBlank
    private String app;

    /**
     * URI для которого был осуществлен запрос
     */
    @Column
    @URL
    private String uri;

    /**
     * IP-адрес пользователя, осуществившего запрос
     */
    @Column
    @Pattern(regexp = "[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]\\.[0-9]")
    private String ip;

    /**
     * Дата и время, когда был совершен запрос к эндпоинту (в формате "yyyy-MM-dd HH:mm:ss")
     */
    @Column
    @Timestamp
    private LocalDateTime time;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EndpointHit that = (EndpointHit) o;
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
