package com.miniblog.like.service.outbox;

import com.miniblog.like.model.OutboxEvent;
import com.miniblog.like.repository.outbox.OutboxEventRepository;
import com.miniblog.like.util.SagaStatus;
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
    public List<OutboxEvent> pollPendingEvents(){
        List<OutboxEvent> unprocessedEvents = outboxEventRepository.findByProcessedFalseAndSagaStatus(SagaStatus.CREATED);
        unprocessedEvents.forEach(outboxEventProcessor::processEvent);

        return unprocessedEvents;
    }
}
