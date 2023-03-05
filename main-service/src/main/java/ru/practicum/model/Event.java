package ru.practicum.model;

import lombok.*;
import lombok.experimental.FieldDefaults;
import ru.practicum.dto.Location;
import ru.practicum.model.enums.State;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "events")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Long id;
    @Column(nullable = false, length = 2000)
    String annotation;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    Category category;
    @Column
    Long confirmedRequests;
    @Column
    LocalDateTime createdOn;
    @Column(length = 7000)
    String description;
    @Column(nullable = false)
    LocalDateTime eventDate;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "initiator_id", nullable = false)
    User initiator;
    @Embedded
    @AttributeOverrides(value = {
            @AttributeOverride(name = "lat", column = @Column(name = "lat")),
            @AttributeOverride(name = "lon", column = @Column(name = "lon"))
    })
    Location location;
    @Column(nullable = false)
    Boolean paid;
    @Column
    Long participantLimit;
    @Column
    LocalDateTime publishedOn;
    @Column
    Boolean requestModeration;
    @Column
    @Enumerated(EnumType.STRING)
    State state;
    @Column(nullable = false)
    String title;
    @Column
    Long views;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id)
                && Objects.equals(annotation, event.annotation)
                && Objects.equals(category, event.category)
                && Objects.equals(confirmedRequests, event.confirmedRequests)
                && Objects.equals(createdOn, event.createdOn)
                && Objects.equals(description, event.description)
                && Objects.equals(eventDate, event.eventDate)
                && Objects.equals(initiator, event.initiator)
                && Objects.equals(location, event.location)
                && Objects.equals(paid, event.paid)
                && Objects.equals(participantLimit, event.participantLimit)
                && Objects.equals(publishedOn, event.publishedOn)
                && Objects.equals(requestModeration, event.requestModeration)
                && state == event.state && Objects.equals(title, event.title)
                && Objects.equals(views, event.views);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, annotation, category, confirmedRequests, createdOn, description, eventDate,
                initiator, location, paid, participantLimit, publishedOn, requestModeration, state, title, views);
    }
}