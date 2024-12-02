package com.miniblog.post.mapper;

import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.model.Post;
import com.miniblog.post.serializer.AvroJsonSerializer;
import com.miniblog.post.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxMapper {
    private final AvroJsonSerializer avroJsonSerializer;
    private final PostCreatedEventMapper postCreatedEventMapper;
    private final PostUpdatedEventMapper postUpdatedEventMapper;
    private final PostDeletedEventMapper postDeletedEventMapper;

    public OutboxEvent toOutboxEvent(Post post, ProducedEventType eventType) {
        SpecificRecordBase event;

        switch (eventType) {
            case POST_CREATE -> event = postCreatedEventMapper.toEntity(post);
            case POST_UPDATE -> event = postUpdatedEventMapper.toEntity(post);
            case POST_DELETE -> event = postDeletedEventMapper.toEntity(post);
            default -> throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
        }
        String payload = avroJsonSerializer.serialize(event);
        return OutboxEvent.builder()
                .postUuid(post.getPostUuid())
                .eventType(eventType)
                .payload(payload)
                .build();

    }
}
