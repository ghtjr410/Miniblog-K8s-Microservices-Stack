package com.miniblog.viewcount.service.outbox;

import com.miniblog.viewcount.mapper.outbox.OutboxMapper;
import com.miniblog.viewcount.model.OutboxEvent;
import com.miniblog.viewcount.model.Viewcount;
import com.miniblog.viewcount.repository.outbox.OutboxEventRepository;
import com.miniblog.viewcount.util.ProducedEventType;
import com.miniblog.viewcount.util.SagaStatus;
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

    public void createOutboxEvent(Viewcount viewcount, ProducedEventType eventType) {
        OutboxEvent outboxEvent = outboxMapper.createToEntity(viewcount, eventType);
        outboxEventRepository.save(outboxEvent);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateStatus(String eventUuid, SagaStatus expectedStatus, SagaStatus newStatus, Boolean processed) {
        boolean updated = outboxEventRepository.updateSagaStatus(UUID.fromString(eventUuid), new SagaStatus[]{expectedStatus}, newStatus, processed);
        if(!updated) {
            log.info("상태 업데이트 실패");
        }
        return updated;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markEventAsCompleted(OutboxEvent outboxEvent) {
        updateStatus(outboxEvent.getEventUuid().toString(), SagaStatus.PROCESSING, SagaStatus.COMPLETED, true);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markEventAsFailed(OutboxEvent outboxEvent) {
        updateStatus(outboxEvent.getEventUuid().toString(), SagaStatus.PROCESSING, SagaStatus.FAILED, null);
    }
}
