package com.miniblog.viewcount.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.viewcount.avro.ViewcountUpdatedEvent;
import com.miniblog.viewcount.model.OutboxEvent;
import com.miniblog.viewcount.producer.EventProducer;
import com.miniblog.viewcount.tracing.SpanFactory;
import com.miniblog.viewcount.util.ProducedEventType;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ViewcountUpdatedEventHandler implements EventProducerHandler {

    private final EventProducer eventProducer;
    private final ObjectMapper objectMapper;
    private final Tracer tracer;
    private final SpanFactory spanFactory;

    @Value("${viewcount.updated.event.topic.name}")
    private String viewcountUpdatedTopicName;

    @Override
    public void handleEvent(OutboxEvent outboxEvent) throws Exception{
        // 1. 페이로드 역직렬화
        SpecificRecordBase eventObject = objectMapper.readValue(outboxEvent.getPayload(), ViewcountUpdatedEvent.class);
        // 2. Span 생성
        Span newSpan = spanFactory.createSpanFromTraceId(outboxEvent.getTraceId());

        // 3. 이벤트 발행
        try (Tracer.SpanInScope ws = tracer.withSpan(newSpan)){
            eventProducer.publishEvent(viewcountUpdatedTopicName, outboxEvent.getEventUuid().toString(), eventObject, newSpan);
        } finally {
            newSpan.end();
        }
    }

    @Override
    public ProducedEventType getEventType() {
        return ProducedEventType.VIEWCOUNT_UPDATE;
    }
}
