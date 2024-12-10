package com.miniblog.like.config.consume;

import lombok.extern.slf4j.Slf4j;
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
                (consumerRecord, exception) -> {
                    log.error("최종 재시도 실패 : eventId={}, eventType={}, error={}",
                            consumerRecord.key(),
                            consumerRecord.value().getClass().getSimpleName(),
                            exception.getMessage());
                },
                backOff
        );

        defaultErrorHandler.addNotRetryableExceptions(SerializationException.class);
        defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("재시도 중 : 시도 횟수 = {}, 예외 = {} ", deliveryAttempt, ex.getMessage());
        });
        return defaultErrorHandler;
    }
}
