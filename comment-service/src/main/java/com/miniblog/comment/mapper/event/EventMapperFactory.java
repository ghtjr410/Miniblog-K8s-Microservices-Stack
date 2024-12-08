package com.miniblog.comment.mapper.event;

import com.miniblog.comment.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapperFactory {
    private final CommentCreatedEventMapper commentCreatedEventMapper;
    private final CommentUpdatedEventMapper commentUpdatedEventMapper;
    private final CommentDeletedEventMapper commentDeletedEventMapper;

    public EventMapper getMapper(ProducedEventType eventType) {
        return switch (eventType) {
            case COMMENT_CREATED -> commentCreatedEventMapper;
            case COMMENT_UPDATED -> commentUpdatedEventMapper;
            case COMMENT_DELETED -> commentDeletedEventMapper;
            default ->  throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
        };
    }
}