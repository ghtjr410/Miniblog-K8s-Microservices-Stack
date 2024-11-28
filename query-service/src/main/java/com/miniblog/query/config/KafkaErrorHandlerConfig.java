package com.miniblog.query.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
@Slf4j
public class KafkaErrorHandlerConfig { // Kafka 에러 핸들러를 설정

    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(1000L);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(10000L);

        // 아직 예외 상황이 없어서 구현하진 않지만 향후 필요하다면 구현할것
//        // Dead Letter Publishing Recoverer 설정
//        DeadLetterPublishingRecoverer deadLetterPublishingRecoverer = new DeadLetterPublishingRecoverer(kafkaTemplate(),
//                (record, exception) -> {
//                    // 원본 토픽 이름에 '.DLT'를 추가하여 Dead Letter 토픽 지정
//                    return new TopicPartition(record.topic() + ".DLT", record.partition());
//                });

        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(
                (consumerRecord, exception) -> handleFailure(consumerRecord, exception),
                backOff
        );
        // SerializationException을 무시하고 다음 메시지로 넘어가도록 설정
        defaultErrorHandler.addNotRetryableExceptions(SerializationException.class);
        defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("재시도 중 : 시도 횟수 = {}, 예외 = {} ", deliveryAttempt, ex.getMessage());
        });
        return defaultErrorHandler;
    }
    private void handleFailure(ConsumerRecord<?, ?> consumerRecord, Exception exception) {
        String eventId = (String) consumerRecord.key();
        Object event = consumerRecord.value();
        log.error("최종 재시도 실패 : eventId={}, eventType={}, error={}",
                eventId, event != null ? event.getClass().getSimpleName() : "null", exception.getMessage());
        // 실패한 이벤트를 별도의 처리(예: Dead Letter Queue)에 추가
        // deadLetterQueueService.sendToDeadLetterQueue(consumerRecord, exception);
    }


}
