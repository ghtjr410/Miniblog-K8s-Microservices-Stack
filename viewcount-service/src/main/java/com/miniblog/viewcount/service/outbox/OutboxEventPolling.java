package com.miniblog.viewcount.service.outbox;

import com.miniblog.viewcount.model.OutboxEvent;
import com.miniblog.viewcount.repository.outbox.OutboxEventRepository;
import com.miniblog.viewcount.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class OutboxEventPolling {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventProcessor outboxEventProcessor;

    @Scheduled(fixedRate = 1000)
    public List<OutboxEvent> pollPendingEvents() {
        List<OutboxEvent> unprocessedEvents = outboxEventRepository.findByProcessedFalseAndSagaStatus(SagaStatus.CREATED);

        unprocessedEvents.stream()
                .forEach(event -> Thread.ofVirtual().start(() -> outboxEventProcessor.processEvent(event)));

        return unprocessedEvents;
    }
}
