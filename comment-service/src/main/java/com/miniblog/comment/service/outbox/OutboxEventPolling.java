package com.miniblog.comment.service.outbox;

import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.repository.outbox.OutboxEventRepository;
import com.miniblog.comment.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.Executors;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class OutboxEventPolling {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventProcessor outboxEventProcessor;

    @Scheduled(fixedRate = 1000)
    public List<OutboxEvent> pollPendingEvents() {
        // 처리되지 않은 이벤트 조회
        List<OutboxEvent> unprocessedEvents = outboxEventRepository.findByProcessedFalseAndSagaStatus(SagaStatus.CREATED);
        // 가상 스레드 풀 생성
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()){
            // 가상 스레드 풀에서 이벤트 처리 작업 제출
            unprocessedEvents.forEach(event ->
                    executor.submit(() -> outboxEventProcessor.processEvent(event))
            );
        }

        return unprocessedEvents;
    }
}