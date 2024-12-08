package com.miniblog.profile.mapper.outbox;

import com.miniblog.profile.mapper.event.EventMapper;
import com.miniblog.profile.mapper.event.EventMapperFactory;
import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.model.Profile;
import com.miniblog.profile.serializer.AvroJsonSerializer;
import com.miniblog.profile.util.ProducedEventType;
import com.miniblog.profile.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMapper {
    private final TracerUtility tracerUtility;
    private final EventMapperFactory eventMapperFactory;
    private final AvroJsonSerializer avroJsonSerializer;

    public OutboxEvent toOutboxEvent(Profile profile, ProducedEventType eventType) {
        String traceId = tracerUtility.getTraceId();
        EventMapper mapper = eventMapperFactory.getMapper(eventType);
        SpecificRecordBase event = mapper.createToEvent(profile);
        String payload = avroJsonSerializer.serialize(event);
        return OutboxEvent.builder()
                .traceId(traceId)
                .profileUuid(profile.getProfileUuid())
                .eventType(eventType)
                .payload(payload)
                .build();

    }
}

