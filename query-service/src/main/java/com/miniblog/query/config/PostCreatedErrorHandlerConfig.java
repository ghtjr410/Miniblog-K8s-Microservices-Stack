package com.miniblog.query.config;

import com.miniblog.post.avro.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class PostCreatedErrorHandlerConfig {

    @Bean
    public DefaultErrorHandler postCreatedEventErrorHandler() {
        ExponentialBackOffWithMaxRetries backOff = new ExponentialBackOffWithMaxRetries(3);
        backOff.setInitialInterval(1000L);
        backOff.setMultiplier(1.0);
        backOff.setMaxInterval(1000L);

        DefaultErrorHandler defaultErrorHandler = new DefaultErrorHandler(
                (consumerRecord, exception) -> {
                    String eventId = (String) consumerRecord.key();
                    PostCreatedEvent postCreatedEvent = (PostCreatedEvent) consumerRecord.value();
                    log.error("최종 재시도 실패. 보상 트랜잭션을 수행합니다. eventId={}, error={}", eventId, exception.getMessage());
                },
                backOff
        );
        defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempt) -> {
            log.warn("재시도 시도 중입니다. 시도 횟수: {}, 예외: {}", deliveryAttempt, ex.getMessage());
        });

        return defaultErrorHandler;
    }
}
