package com.miniblog.comment.service;

import com.miniblog.comment.mapper.OutboxMapper;
import com.miniblog.comment.model.Comment;
import com.miniblog.comment.model.OutboxEvent;
import com.miniblog.comment.repository.OutboxEventRepository;
import com.miniblog.comment.util.EventType;
import com.miniblog.comment.util.SagaStatus;
import com.miniblog.comment.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;
    private final OutboxMapper outboxMapper;
    private final TracerUtility tracerUtility;

    public void createOutboxEvent(Comment comment, EventType eventType) {
        String traceId = tracerUtility.getTraceId();
        OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(comment, eventType);
        outboxEvent.setTraceId(traceId);
        outboxEventRepository.save(outboxEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateStatus(String eventUuid, SagaStatus expectedStatus, SagaStatus newStatus, Boolean processed) {
        boolean updated = outboxEventRepository.updateSagaStatus(eventUuid, new SagaStatus[]{expectedStatus}, newStatus, processed);
        if (!updated) {
//            log.warn("Failed to update status from {} to {} for event UUID {}", expectedStatus, newStatus, eventUuid);
            log.warn("상태변경실패 {}, {}, {}", expectedStatus, newStatus, eventUuid);
        } else {
            log.info("상태변경성공 {}, {}, {}", expectedStatus, newStatus, eventUuid);
        }
        return updated;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markEventAsCompleted(OutboxEvent outboxEvent) {
        log.info("성공했을때 상태변경");
        updateStatus(outboxEvent.getEventUuid(), SagaStatus.PROCESSING, SagaStatus.COMPLETED, true);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markEventAsFailed(OutboxEvent outboxEvent) {
        log.info("실패했을때 상태변경");
        updateStatus(outboxEvent.getEventUuid(), SagaStatus.PROCESSING, SagaStatus.FAILED, null);
    }
}
