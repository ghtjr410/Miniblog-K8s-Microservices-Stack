package com.miniblog.comment.config.consume;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.common.errors.SerializationException;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
@Slf4j
public class KafkaErrorHandlerConfig {
    @Bean
    public DefaultErrorHandler kafkaErrorHandler() {
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(1000L);
        backOff.setMultiplier(2.0);
        backOff.setMaxInterval(10000L);

        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(
                (consumerRecord, exception) -> handleFailure(consumerRecord, exception),
                backOff
        );
        defaultErrorHandler.addNotRetryableExceptions(SerializationException.class);
        defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("재시도 중 : 시도 횟수 = {}, 예외 = {} ", deliveryAttempt, ex.getMessage());
        });
        return defaultErrorHandler;
    }

    private void handleFailure(ConsumerRecord<?, ?> consumerRecord, Exception exception) {
        String eventUuid = (String) consumerRecord.key();
        Object event = consumerRecord.value(); // todo: SpecificRecordBase로 바꿔야함
        log.error("최종 재시도 실패 : eventId={}, eventType={}, error={}",
                eventUuid, event != null ? event.getClass().getSimpleName() : "null", exception.getMessage());
    }
}
