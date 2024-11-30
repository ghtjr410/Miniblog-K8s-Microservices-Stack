package com.miniblog.comment.mapper;

import com.miniblog.comment.model.Comment;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.serializer.AvroJsonSerializer;
import com.miniblog.comment.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMapper {
    private final AvroJsonSerializer avroJsonSerializer;
    private final CommentCreatedEventMapper commentCreatedEventMapper;
    private final CommentUpdatedEventMapper commentUpdatedEventMapper;
    private final CommentDeletedEventMapper commentDeletedEventMapper;

    public OutboxEvent toOutboxEvent(Comment comment, ProducedEventType eventType){
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
    }
}
