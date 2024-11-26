package com.miniblog.viewcount.mapper;

import com.miniblog.viewcount.avro.ViewcountUpdatedEvent;
import com.miniblog.viewcount.model.OutboxEvent;
import com.miniblog.viewcount.model.Viewcount;
import com.miniblog.viewcount.serializer.AvroJsonSerializer;
import com.miniblog.viewcount.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class OutboxMapper {
    private final AvroJsonSerializer avroJsonSerializer;
    private final ViewcountUpdatedEventMapper viewcountUpdatedEventMapper;

    public OutboxEvent toOutboxEvent(Viewcount viewcount, ProducedEventType eventType) {
        ViewcountUpdatedEvent viewcountUpdatedEvent = viewcountUpdatedEventMapper.toEntity(viewcount);
        String payload = avroJsonSerializer.serialize(viewcountUpdatedEvent);

        return OutboxEvent.builder()
                .postUuid(viewcount.getPostUuid())
                .eventType(eventType)
                .payload(payload)
                .build();
    }
}
