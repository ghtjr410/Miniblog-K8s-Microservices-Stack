package com.miniblog.like.mapper.event;

import com.miniblog.like.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapperFactory {
    private final LikeCreatedEventMapper likeCreatedEventMapper;
    private final LikeDeletedEventMapper likeDeletedEventMapper;

    public EventMapper getMapper(ProducedEventType eventType) {
        return switch (eventType) {
            case LIKE_CREATED -> likeCreatedEventMapper;
            case LIKE_DELETED -> likeDeletedEventMapper;
            default -> throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
        };
    }
}
