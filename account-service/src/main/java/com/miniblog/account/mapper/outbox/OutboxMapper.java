package com.miniblog.account.mapper.outbox;

import com.miniblog.account.mapper.event.AccountDeletedEventMapper;
import com.miniblog.account.model.OutboxEvent;
import com.miniblog.account.serializer.AvroJsonSerializer;
import com.miniblog.account.util.ProducedEventType;
import com.miniblog.account.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMapper {
    private final TracerUtility tracerUtility;
    private final AccountDeletedEventMapper accountDeletedEventMapper;
    private final AvroJsonSerializer avroJsonSerializer;

    public OutboxEvent createToEntity(String userUuid, ProducedEventType eventType) {
        String traceId = tracerUtility.getTraceId();
        SpecificRecordBase event = accountDeletedEventMapper.createToEvent(userUuid);
        String payload = avroJsonSerializer.serialize(event);
        return OutboxEvent.builder()
                .traceId(traceId)
                .eventType(eventType)
                .payload(payload)
                .build();
    }
}
