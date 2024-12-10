package com.miniblog.comment.service.outbox;

import com.miniblog.comment.mapper.outbox.OutboxMapper;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.repository.outbox.OutboxEventRepository;
import com.miniblog.comment.util.ProducedEventType;
import com.miniblog.comment.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxMapper outboxMapper;

    public void createOutboxEvent(Comment comment, ProducedEventType eventType) {
        OutboxEvent outboxEvent = outboxMapper.createToEntity(comment, eventType);
        outboxEventRepository.save(outboxEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateStatus(UUID eventUuid, SagaStatus expectedStatus, SagaStatus newStatus, Boolean processed) {
        boolean updated = outboxEventRepository.updateSagaStatus(eventUuid, new SagaStatus[]{expectedStatus}, newStatus, processed);
        if(!updated) {
            log.info("상태 업데이트 실패");
        }
        return updated;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markEventAsCompleted(OutboxEvent outboxEvent) {
        updateStatus(outboxEvent.getEventUuid(), SagaStatus.PROCESSING, SagaStatus.COMPLETED, true);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markEventAsFailed(OutboxEvent outboxEvent) {
        updateStatus(outboxEvent.getEventUuid(), SagaStatus.PROCESSING, SagaStatus.FAILED, null);
    }
}
