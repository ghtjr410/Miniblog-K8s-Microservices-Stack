package com.miniblog.like.handler.produce;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.like.model.OutboxEvent;
import com.miniblog.like.producer.EventProducer;
import com.miniblog.like.tracing.SpanFactory;
import com.miniblog.like.util.ProducedEventType;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;

@RequiredArgsConstructor
public class AbstractEventProducerHandler <T extends SpecificRecordBase> implements EventProducerHandler {
    protected final EventProducer eventProducer;
    protected final ObjectMapper objectMapper;
    protected final Tracer tracer;
    protected final SpanFactory spanFactory;
    protected final Class<T> eventClass;
    protected final ProducedEventType eventType;

    protected String topicName;

    @Override
    public void handleEvent(OutboxEvent outboxEvent) throws Exception {
        // 1. 페이로드 역직렬화
        T event = objectMapper.readValue(outboxEvent.getPayload(), eventClass);
        // 2. Span 생성
        Span newSpan = spanFactory.createSpanFromTraceId(outboxEvent.getTraceId());
        // 3. 이벤트 발행
        try (Tracer.SpanInScope ws = tracer.withSpan(newSpan)) {
            eventProducer.publishEvent(topicName, outboxEvent.getEventType().toString(), event, newSpan);
        } finally {
            newSpan.end();
        }
    }

    @Override
    public ProducedEventType getEventType() {
        return eventType;
    }
}
