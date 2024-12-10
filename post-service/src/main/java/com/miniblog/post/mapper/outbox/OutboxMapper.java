package com.miniblog.post.mapper.outbox;

import com.miniblog.post.mapper.event.EventMapper;
import com.miniblog.post.mapper.event.EventMapperFactory;
import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.model.Post;
import com.miniblog.post.serializer.AvroJsonSerializer;
import com.miniblog.post.util.ProducedEventType;
import com.miniblog.post.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMapper {
    private final TracerUtility tracerUtility;
    private final EventMapperFactory eventMapperFactory;
    private final AvroJsonSerializer avroJsonSerializer;

    public OutboxEvent createToEntity(Post post, ProducedEventType eventType) {
        String traceId = tracerUtility.getTraceId();
        EventMapper mapper = eventMapperFactory.getMapper(eventType);
        SpecificRecordBase event = mapper.createToEvent(post);
        String payload = avroJsonSerializer.serialize(event);
        return OutboxEvent.builder()
                .traceId(traceId)
                .postUuid(post.getPostUuid())
                .eventType(eventType)
                .payload(payload)
                .build();

    }
}
