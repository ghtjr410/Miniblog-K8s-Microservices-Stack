package com.miniblog.query.consumer;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.query.service.PostCreatedEventService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PostCreatedConsumer {
    private final PostCreatedEventService postCreatedEventService;

    @KafkaListener(
            topics = "${post.created.event.topic.name}",
            groupId = "${query.consumer.group-id}",
            containerFactory = "postCreatedEventListenerContainerFactory")
    public void consume(ConsumerRecord<String, PostCreatedEvent> record, Acknowledgment ack) {
        log.info("Received message: key={}, value={}", record.key(), record.value());

        String eventUuid = record.key();
        PostCreatedEvent postCreatedEvent = record.value();
        // 여기서 서비스 로직하면댐
        postCreatedEventService.processEvent(eventUuid, postCreatedEvent);
        ack.acknowledge();
    }

}
