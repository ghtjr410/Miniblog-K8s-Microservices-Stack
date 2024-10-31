package com.miniblog.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.producer.PostCreatedEventProducer;
import com.miniblog.post.repository.OutboxEventRepository;
import com.miniblog.post.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventProcessor {
    private final OutboxEventRepository outboxEventRepository;
    private final PostCreatedEventProducer postCreatedEventProducer;
    private final ObjectMapper objectMapper;

    public void processEvent(OutboxEvent outboxEvent) {
        try {
            // Saga 상태를 PROCESSING으로 업데이트
            outboxEvent.setSagaStatus(SagaStatus.PROCESSING);
            outboxEventRepository.save(outboxEvent);

            PostCreatedEvent postCreatedEvent = objectMapper.readValue(outboxEvent.getPayload(), PostCreatedEvent.class);

            postCreatedEventProducer.publishEvent(outboxEvent.getEventUuId(), postCreatedEvent, outboxEvent);
        } catch (Exception e){
            log.error("Error processing OutboxEvent : {}", e.getMessage());
        }

    }

}
