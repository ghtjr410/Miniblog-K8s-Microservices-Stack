package com.miniblog.comment.service.outbox;

import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.repository.outbox.OutboxEventRepository;
import com.miniblog.comment.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class OutboxEventPolling {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxEventProcessor outboxEventProcessor;

    @Scheduled(fixedRate = 1000)
    public List<OutboxEvent> pollPendingEvents() {
        List<OutboxEvent> unprocessedEvents = outboxEventRepository.findByProcessedFalseAndSagaStatus(SagaStatus.CREATED);
        unprocessedEvents.forEach(outboxEventProcessor::processEvent);

        return unprocessedEvents;
    }
}