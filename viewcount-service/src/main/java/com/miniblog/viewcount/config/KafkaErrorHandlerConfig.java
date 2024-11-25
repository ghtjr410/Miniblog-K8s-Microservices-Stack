package com.miniblog.viewcount.config;

import lombok.extern.slf4j.Slf4j;
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
        backOff.setMaxInterval(1000L);

        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(
                (consumerRecord, exception) -> {
                    String eventId = (String) consumerRecord.key();
                    Object event = consumerRecord.value();
                    log.error("최종 재시도 실패 : eventId={}, eventType={}, error={}",
                            eventId, event.getClass().getSimpleName(), exception.getMessage());
                },
                backOff
        );
        defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("재시도 중 : 시도 횟수 = {}, 예외 = {} ", deliveryAttempt, ex.getMessage());
        });
        return defaultErrorHandler;
    }
}
