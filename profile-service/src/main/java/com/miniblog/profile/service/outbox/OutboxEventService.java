package com.miniblog.profile.service.outbox;

import com.miniblog.profile.mapper.OutboxMapper;
import com.miniblog.profile.model.OutboxEvent;
import com.miniblog.profile.model.Profile;
import com.miniblog.profile.repository.outbox.OutboxEventRepository;
import com.miniblog.profile.util.ProducedEventType;
import com.miniblog.profile.util.SagaStatus;
import com.miniblog.profile.util.TracerUtility;
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
    private final TracerUtility tracerUtility;

    public void createEntity(Profile profile, ProducedEventType eventType) {
        String traceId = tracerUtility.getTraceId();
        OutboxEvent outboxEvent = outboxMapper.toOutboxEvent(profile, eventType);
        outboxEvent.setTraceId(traceId);
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
