package com.miniblog.viewcount.consumer;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.viewcount.service.ConsumedEventProcessor;
import com.miniblog.viewcount.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostCreatedConsumer {
    private final ConsumedEventProcessor receivedEventProcessor;

    @KafkaListener(
            topics = "${post.created.event.topic.name}",
            groupId = "${viewcount.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, PostCreatedEvent> record, Acknowledgment ack) {
        log.info("Received PostCreatedEvent: key={}, value={}", record.key(), record.value());
        UUID eventUuid = UUID.fromString(record.key());
        PostCreatedEvent postCreatedEvent = record.value();

        receivedEventProcessor.processEvent(eventUuid, postCreatedEvent, ConsumedEventType.POST_CREATE);
        ack.acknowledge();
    }
}
