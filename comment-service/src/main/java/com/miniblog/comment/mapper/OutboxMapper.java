package com.miniblog.comment.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.util.EventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxMapper {
    private final AvroJsonSerializer avroJsonSerializer;
    private final CommentCreatedEventMapper commentCreatedEventMapper;
    private final CommentUpdatedEventMapper commentUpdatedEventMapper;
    private final CommentDeletedEventMapper commentDeletedEventMapper;

    public OutboxEvent toOutboxEvent(Comment comment, EventType eventType) throws JsonProcessingException {
        try {
            SpecificRecordBase event;

            switch (eventType) {
                case COMMENT_CREATED:
                    event = commentCreatedEventMapper.toEntity(comment);
                    break;
                case COMMENT_UPDATED:
                    event = commentUpdatedEventMapper.toEntity(comment);
                    break;
                case COMMENT_DELETED:
                    event = commentDeletedEventMapper.toEntity(comment);
                    break;
                default:
                    throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
            }

            String payload = avroJsonSerializer.serialize(event);

            return OutboxEvent.builder()
                    .commentUuid(comment.getCommentUuid())
                    .eventType(eventType)
                    .payload(payload)
                    .build();
        } catch (IOException ex) {
            log.error("OutboxMapper toOutboxEvent : {}", ex.getMessage(), ex);
            throw new RuntimeException("OutboxEvent 생성 실패", ex);
        }
    }
}
