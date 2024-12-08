package com.miniblog.comment.mapper.outbox;

import com.miniblog.comment.mapper.event.EventMapper;
import com.miniblog.comment.mapper.event.EventMapperFactory;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.serializer.AvroJsonSerializer;
import com.miniblog.comment.util.ProducedEventType;
import com.miniblog.comment.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxMapper {
    private final TracerUtility tracerUtility;
    private final EventMapperFactory eventMapperFactory;
    private final AvroJsonSerializer avroJsonSerializer;

    public OutboxEvent toOutboxEvent(Comment comment, ProducedEventType eventType){
        String traceId = tracerUtility.getTraceId();
        EventMapper mapper = eventMapperFactory.getMapper(eventType);
        SpecificRecordBase event = mapper.createToEvent(comment);
        String payload = avroJsonSerializer.serialize(event);

        return OutboxEvent.builder()
                .traceId(traceId)
                .commentUuid(comment.getCommentUuid())
                .eventType(eventType)
                .payload(payload)
                .build();
    }
}
