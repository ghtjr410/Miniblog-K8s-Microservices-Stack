package com.miniblog.query.consumer;

import com.miniblog.profile.avro.ProfileCreatedEvent;
import com.miniblog.query.service.ProfileCreatedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileCreatedConsumer {
    private final ProfileCreatedEventService profileCreatedEventService;

    @KafkaListener(
            topics = "${profile.created.event.topic.name}",
            groupId = "${query.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, ProfileCreatedEvent> record, Acknowledgment ack) {
        log.info("Received ProfileCreatedEvent: key={}, value={}", record.key(), record.value());

        String eventUuid = record.key();
        ProfileCreatedEvent profileCreatedEvent = record.value();

        profileCreatedEventService.processEvent(eventUuid, profileCreatedEvent);
        ack.acknowledge();
    }
}
