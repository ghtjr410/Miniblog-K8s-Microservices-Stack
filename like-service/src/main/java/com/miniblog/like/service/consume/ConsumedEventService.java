package com.miniblog.like.service.consume;

import com.miniblog.like.mapper.consume.ConsumedMapper;
import com.miniblog.like.model.ConsumedEvent;
import com.miniblog.like.repository.consume.ConsumedEventRepository;
import com.miniblog.like.util.ConsumedEventType;
import com.miniblog.like.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumedEventService {
    private final ConsumedEventRepository consumedEventRepository;
    private final ConsumedMapper consumedEventMapper;

    public ConsumedEvent findOrInitializeEvent(UUID eventUuid, ConsumedEventType eventType) {
        // 여기서 eventUuid를 기준으로 processed가 false인것을 가져와야한다
        Optional<ConsumedEvent> optionalConsumedEvent = consumedEventRepository.findByProcessedFalseAndEventUuid(eventUuid);

        if (optionalConsumedEvent.isEmpty()) {
            // 신규 이벤트 생성 후 바로 PROCESSING 상태 (동기적이기 때문)
            log.info("새로운 이벤트 생성: eventUuid={}, eventType={}", eventUuid, eventType);
            return createNewEvent(eventUuid, eventType);
        }

        ConsumedEvent existingEvent = optionalConsumedEvent.get();
        if (existingEvent.getSagaStatus() == SagaStatus.FAILED) {
            // 실패한 이벤트 -> 재처리할 수 있도록 반환
            log.info("재처리 시도: eventUuid={}, eventType={}", eventUuid, eventType);
            updateStatus(eventUuid, new SagaStatus[]{SagaStatus.FAILED}, SagaStatus.RETRYING, null);
            return existingEvent;
        } else {
            // PROCESSING, RETRYING, COMPLETED 상태는 모두 이미 처리 중이거나 처리 완료된 이벤트
            // 멱등성 상 재처리 불필요
            log.info("멱등성 상 재처리 불필요: eventUuid={}", eventUuid);
            return null;
        }
    }

    public void markEventAsCompleted(UUID eventUuid) {
        updateStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.COMPLETED, true);
    }

    public void markEventAsFailed(UUID eventUuid) {
        updateStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.FAILED, null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ConsumedEvent createNewEvent(UUID eventUuid, ConsumedEventType eventType) {
        ConsumedEvent consumedEvent = consumedEventMapper.createToEntity(eventUuid, eventType);
        consumedEventRepository.save(consumedEvent);
        return consumedEvent;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateStatus(UUID eventUuid, SagaStatus[] expectedStatus, SagaStatus newStatus, Boolean processed) {
        boolean updated = consumedEventRepository.updateSagaStatus(eventUuid, expectedStatus, newStatus, processed);
        log.info(updated ? "상태 업데이트 성공" : "상태 업데이트 실패");
        return updated;
    }
}
