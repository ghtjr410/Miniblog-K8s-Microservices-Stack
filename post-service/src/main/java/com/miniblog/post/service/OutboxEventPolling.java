package com.miniblog.post.service;

import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.repository.OutboxEventRepository;
import com.miniblog.post.util.SagaStatus;
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
        // 각 이벤트를 처리
        unprocessedEvents.forEach(outboxEventProcessor::processEvent);
    }
}
