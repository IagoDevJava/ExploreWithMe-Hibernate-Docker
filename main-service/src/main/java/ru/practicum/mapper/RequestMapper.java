package ru.practicum.mapper;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.dto.ParentRequestDto;
import ru.practicum.model.enums.Status;
import ru.practicum.model.Event;
import ru.practicum.model.Request;
import ru.practicum.model.User;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.mapper.DateTimeMapper.toStringDateTime;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class RequestMapper {

    public static Request toRequest(Event event, User requester) {
        Request request = new Request();
        request.setEvent(event);
        request.setRequester(requester);
        request.setCreated(LocalDateTime.now());
        request.setStatus(event.getRequestModeration() ? Status.PENDING : Status.CONFIRMED);
        return request;
    }

    public static ParentRequestDto toParentRequestDto(Request request) {
        return new ParentRequestDto(
                request.getId(),
                toStringDateTime(request.getCreated()),
                request.getEvent().getId(),
                request.getRequester().getId(),
                request.getStatus().toString()
        );
    }

    public static List<ParentRequestDto> toParentRequestDtoList(List<Request> requests) {
        return requests.stream().map(RequestMapper::toParentRequestDto).collect(Collectors.toList());
    }
}
