package com.miniblog.query.consumer;

import com.miniblog.profile.avro.ProfileUpdatedEvent;
import com.miniblog.query.service.ProfileUpdatedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class ProfileUpdatedConsumer {
    private final ProfileUpdatedEventService profileUpdatedEventService;

    @KafkaListener(
            topics = "${profile.updated.event.topic.name}",
            groupId = "${query.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, ProfileUpdatedEvent> record, Acknowledgment ack) {
        log.info("Received ProfileUpdatedEvent: key={}, value={}", record.key(), record.value());

        String eventUuid = record.key();
        ProfileUpdatedEvent profileUpdatedEvent = record.value();

        profileUpdatedEventService.processEvent(eventUuid, profileUpdatedEvent);
        ack.acknowledge();
    }
}
