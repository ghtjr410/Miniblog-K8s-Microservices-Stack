package com.miniblog.viewcount.consumer;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.viewcount.service.ReceivedEventProcessor;
import com.miniblog.viewcount.util.ReceivedEventType;
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
public class PostDeletedConsumer {
    private final ReceivedEventProcessor receivedEventProcessor;

    @KafkaListener(
            topics = "${post.deleted.event.topic.name}",
            groupId = "${viewcount.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, PostDeletedEvent> record, Acknowledgment ack) {
        log.info("Received PostDeletedEvent: topic={}, key={}, value={}",record.topic() , record.key(), record.value());
        UUID eventUuid = UUID.fromString(record.key());
        PostDeletedEvent postDeletedEvent = record.value();

        receivedEventProcessor.processEvent(eventUuid, postDeletedEvent, PostDeletedEvent.class, ReceivedEventType.POST_DELETE);
        ack.acknowledge();
    }
}
