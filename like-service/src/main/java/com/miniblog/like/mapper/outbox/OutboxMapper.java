package com.miniblog.like.mapper.outbox;

import com.miniblog.like.mapper.event.EventMapper;
import com.miniblog.like.mapper.event.EventMapperFactory;
import com.miniblog.like.model.Like;
import com.miniblog.like.model.OutboxEvent;
import com.miniblog.like.serializer.AvroJsonSerializer;
import com.miniblog.like.util.ProducedEventType;
import com.miniblog.like.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMapper {
    private final TracerUtility tracerUtility;
    private final EventMapperFactory eventMapperFactory;
    private final AvroJsonSerializer avroJsonSerializer;

    public OutboxEvent createToEntity(Like like, ProducedEventType eventType) {
        String traceId = tracerUtility.getTraceId();
        EventMapper mapper = eventMapperFactory.getMapper(eventType);
        SpecificRecordBase event = mapper.createToEvent(like);
        String payload = avroJsonSerializer.serialize(event);
        return OutboxEvent.builder()
                .traceId(traceId)
                .likeUuid(like.getLikeUuid())
                .eventType(eventType)
                .payload(payload)
                .build();
    }
}
