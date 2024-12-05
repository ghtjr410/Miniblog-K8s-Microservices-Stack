package com.miniblog.like.mapper;

import com.miniblog.like.model.Like;
import com.miniblog.like.model.OutboxEvent;
import com.miniblog.like.serializer.AvroJsonSerializer;
import com.miniblog.like.util.ProducedEventType;
import com.miniblog.like.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxMapper {
    private final AvroJsonSerializer avroJsonSerializer;
    private final TracerUtility tracerUtility;
    private final LikeCreatedEventMapper likeCreatedEventMapper;
    private final LikeDeletedEventMapper likeDeletedEventMapper;

    public OutboxEvent createEntity(Like like, ProducedEventType eventType) {
        SpecificRecordBase event;
        String traceId = tracerUtility.getTraceId();
        switch (eventType) {
            case LIKE_CREATED -> event = likeCreatedEventMapper.toEntity(like);
            case LIKE_DELETED -> event = likeDeletedEventMapper.toEntity(like);
            default -> throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
        }
        String payload = avroJsonSerializer.serialize(event);
        return OutboxEvent.builder()
                .likeUuid(like.getLikeUuid())
                .eventType(eventType)
                .payload(payload)
                .traceId(traceId)
                .build();
    }
}
