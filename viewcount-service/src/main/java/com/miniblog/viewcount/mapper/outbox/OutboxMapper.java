package com.miniblog.viewcount.mapper.outbox;

import com.miniblog.viewcount.mapper.event.EventMapper;
import com.miniblog.viewcount.mapper.event.EventMapperFactory;
import com.miniblog.viewcount.model.OutboxEvent;
import com.miniblog.viewcount.model.Viewcount;
import com.miniblog.viewcount.serializer.AvroJsonSerializer;
import com.miniblog.viewcount.util.ProducedEventType;
import com.miniblog.viewcount.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMapper {
    private final TracerUtility tracerUtility;
    private final EventMapperFactory eventMapperFactory;
    private final AvroJsonSerializer avroJsonSerializer;

    public OutboxEvent toOutboxEvent(Viewcount viewcount, ProducedEventType eventType) {
        String traceId = tracerUtility.getTraceId();
        EventMapper mapper = eventMapperFactory.getMapper(eventType);
        SpecificRecordBase event = mapper.createToEvent(viewcount);
        String payload = avroJsonSerializer.serialize(event);
        return OutboxEvent.builder()
                .traceId(traceId)
                .postUuid(viewcount.getPostUuid())
                .eventType(eventType)
                .payload(payload)
                .build();
    }
}
