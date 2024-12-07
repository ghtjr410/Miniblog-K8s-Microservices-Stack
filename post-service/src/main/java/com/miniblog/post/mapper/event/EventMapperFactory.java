package com.miniblog.post.mapper.event;

import com.miniblog.post.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapperFactory {
    private final PostCreatedEventMapper postCreatedEventMapper;
    private final PostUpdatedEventMapper postUpdatedEventMapper;
    private final PostDeletedEventMapper postDeletedEventMapper;

    public EventMapper getMapper(ProducedEventType eventType) {
        return switch (eventType) {
            case POST_CREATE -> postCreatedEventMapper;
            case POST_UPDATE -> postUpdatedEventMapper;
            case POST_DELETE -> postDeletedEventMapper;
            default -> throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
        };
    }
}
