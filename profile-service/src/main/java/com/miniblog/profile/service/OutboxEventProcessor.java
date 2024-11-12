package com.miniblog.profile.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.producer.EventProducer;
import com.miniblog.profile.repository.OutboxEventRepository;
import com.miniblog.profile.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventProcessor {
    private final OutboxEventRepository outboxEventRepository;
    private final EventProducer eventProducer;
    private final ObjectMapper objectMapper;

    @Value("${profile.created.event.topic.name}")
    private String profileCreatedTopicName;

    @Value("${profile.updated.event.topic.name}")
    private String profileUpdatedTopicName;

    public void processProfileCreatedEvent(OutboxEvent outboxEvent) {
        try {
            outboxEvent.setSagaStatus(SagaStatus.PROCESSING);
            outboxEventRepository.save(outboxEvent);
            ProfileCreatedEvent profileCreatedEvent = objectMapper.readValue(outboxEvent.getPayload(), ProfileCreatedEvent.class);

            eventProducer.publishEvent(profileCreatedTopicName, outboxEvent.getEventUuid(), profileCreatedEvent, outboxEvent);
        } catch (Exception ex) {
            log.error("Error processing OutboxEvent : {}", ex.getMessage());
        }
    }

    public void processProfileUpdatedEvent(OutboxEvent outboxEvent) {
        try {
            outboxEvent.setSagaStatus(SagaStatus.PROCESSING);
            outboxEventRepository.save(outboxEvent);
            ProfileUpdatedEvent profileUpdatedEvent = objectMapper.readValue(outboxEvent.getPayload(), ProfileUpdatedEvent.class);

            eventProducer.publishEvent(profileUpdatedTopicName, outboxEvent.getEventUuid(), profileUpdatedEvent, outboxEvent);
        } catch (Exception ex) {
            log.error("Error processing OutboxEvent : {}", ex.getMessage());
        }
    }
}
