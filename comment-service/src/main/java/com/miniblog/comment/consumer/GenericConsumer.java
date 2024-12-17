package com.miniblog.comment.consumer;

import com.miniblog.comment.service.consume.ConsumedEventProcessor;
import com.miniblog.comment.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@RequiredArgsConstructor
@Slf4j
public abstract class GenericConsumer<T extends SpecificRecordBase> {
    private final ConsumedEventProcessor consumedEventProcessor;

    /**
     * KafkaListener 어노테이션에서 토픽 이름을 동적으로 가져오기 위해 SpEL을 사용
     */
    @KafkaListener(
            topics = "#{__listener.topicName()}",
            groupId = "${comment.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    @Transactional
    public void consume(ConsumerRecord<String, T> record, Acknowledgment ack) {
        log.info("Received {}: topic={}, key={}, value={}", getConsumedEventType(), record.topic(), record.key(), record.value());
        UUID eventUuid = UUID.fromString(record.key());
        T event = record.value();

        consumedEventProcessor.processEvent(eventUuid, event, getConsumedEventType());
        ack.acknowledge();
    }

    /**
     * 이벤트 타입을 반환하는 추상 메서드
     */
    protected abstract ConsumedEventType getConsumedEventType();

    /**
     * 토픽 이름을 반환하는 추상 메서드
     */
    protected abstract String topicName();
}
