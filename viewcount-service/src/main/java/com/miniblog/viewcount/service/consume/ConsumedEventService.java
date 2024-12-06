package com.miniblog.viewcount.service.consume;

import com.miniblog.viewcount.mapper.ConsumedEventMapper;
import com.miniblog.viewcount.model.ConsumedEvent;
import com.miniblog.viewcount.repository.consume.ConsumedEventRepository;
import com.miniblog.viewcount.util.ConsumedEventType;
import com.miniblog.viewcount.util.SagaStatus;
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
    private final ConsumedEventMapper consumedEventMapper;

    public ConsumedEvent handleIdempotencyAndUpdateStatus(UUID eventUuid, ConsumedEventType eventType) {
        // 여기서 eventUuid를 기준으로 processed가 false인것을 가져와야한다
        Optional<ConsumedEvent> optionalConsumedEvent = consumedEventRepository.findByProcessedFalseAndEventUuid(eventUuid);
        if(optionalConsumedEvent.isPresent()){
            ConsumedEvent consumedEvent = optionalConsumedEvent.get();
            // 1. 처리중이거나 처리시도가된적있는 이벤트
            // 1-1 처리중인 이벤트
            if (consumedEvent.getSagaStatus() != SagaStatus.FAILED) {
                log.info("처리 중인 이벤트: eventUuid={}", eventUuid);
                return null;
            }
            // 1-2 처리시도가 된적있는 이벤트
            log.info("이번에 실패한 이벤트. 재처리 시도: eventUuid={}", eventUuid);
            updateStatus(eventUuid, new SagaStatus[]{SagaStatus.FAILED}, SagaStatus.RETRYING, null);
            return consumedEvent;

        } else {
            // 2. 한번도 시도된적없는 이벤트
            log.info("새로운 객체 생성: eventUuid={}", eventUuid);
            return createToEntity(eventUuid, eventType);
        }
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ConsumedEvent createToEntity(UUID eventUuid, ConsumedEventType eventType) {
        ConsumedEvent consumedEvent = consumedEventMapper.toEntity(eventUuid, eventType);
        consumedEventRepository.save(consumedEvent);
        return consumedEvent;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateStatus(UUID eventUuid, SagaStatus[] expectedStatus, SagaStatus newStatus, Boolean processed) {
        boolean updated = consumedEventRepository.updateSagaStatus(eventUuid, expectedStatus, newStatus, processed);
        if (!updated) {
            log.info("상태 업데이트 실패");
        } else {
            log.info("상태 업데이트 성공");
        }
        return updated;
    }
}
