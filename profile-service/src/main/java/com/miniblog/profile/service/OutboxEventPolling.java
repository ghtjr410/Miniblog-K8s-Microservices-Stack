package com.miniblog.profile.service;

import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.repository.OutboxEventRepository;
import com.miniblog.profile.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
@Slf4j
public class OutboxEventPolling {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventProcessor outboxEventProcessor;

    @Scheduled(fixedRate = 1000)
    public void pollPendingEvents() {
        // 처리되지 않은 이벤트를 가져오는 작업
        List<OutboxEvent> unprocessedEvents = outboxEventRepository.findByProcessedFalseAndSagaStatus(SagaStatus.CREATED);
        log.info("처리되지않은 outbox events 개수 : {}", unprocessedEvents.size());
        unprocessedEvents.forEach(outboxEvent -> {
            switch (outboxEvent.getEventType()) {
                case PROFILE_CREATED:
                    log.info("Processing PROFILE_CREATED event with ID: {}", outboxEvent.getId());
                    outboxEventProcessor.processProfileCreatedEvent(outboxEvent);
                    break;
                case PROFILE_UPDATED:
                    log.info("Processing PROFILE_UPDATED event with ID: {}", outboxEvent.getId());
                    outboxEventProcessor.processProfileUpdatedEvent(outboxEvent);
                    break;
                default:
                    log.warn("알 수 없는 이벤트 타입: {}", outboxEvent.getEventType());
                    break;
            }
        });
    }
}
