package com.miniblog.query.consumer;

import com.miniblog.query.service.consumedEvent.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;

@RequiredArgsConstructor
@Slf4j
public abstract class GenericConsumer<T extends SpecificRecordBase> {
    private final ConsumedEventProcessor consumedEventProcessor;

    /**
     * KafkaListener 어노테이션에서 토픽 이름을 동적으로 가져오기 위해 SpEL을 사용합니다.
     */
    @KafkaListener(
            topics = "#{__listener.topicName()}",
            groupId = "${query.consumer.group-id}",
            containerFactory = "kafkaListenerContainerFactory")
    public void consume(ConsumerRecord<String, T> record, Acknowledgment ack) {
        log.info("Received {}: topic={}, key={}, value={}", getConsumedEventType(), record.topic(), record.key(), record.value());
        String eventUuid = record.key();
        T event = record.value();

        consumedEventProcessor.processEvent(eventUuid, event, getConsumedEventType());
        ack.acknowledge();
    }

    /**
     * 이벤트 타입을 반환하는 추상 메서드입니다.
     */
    protected abstract ConsumedEventType getConsumedEventType();

    /**
     * 토픽 이름을 반환하는 추상 메서드입니다.
     */
    protected abstract String topicName();
}
