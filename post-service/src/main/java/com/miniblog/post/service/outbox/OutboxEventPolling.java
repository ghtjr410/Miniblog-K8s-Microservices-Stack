package com.miniblog.post.service.outbox;

import com.miniblog.post.model.OutboxEvent;
import com.miniblog.post.repository.outbox.OutboxEventRepository;
import com.miniblog.post.util.SagaStatus;
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
