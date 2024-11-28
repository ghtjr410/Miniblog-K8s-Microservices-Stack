package com.miniblog.query.service.consumedEvent;

import com.miniblog.query.model.ConsumedEvent;
import com.miniblog.query.repository.consumedEvent.ConsumedEventRepository;
import com.miniblog.query.util.ConsumedEventType;
import com.miniblog.query.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumedEventService {
    private final ConsumedEventRepository consumedEventRepository;

    public ConsumedEvent handleIdempotencyAndUpdateStatus(String eventUuid, ConsumedEventType eventType) {
        // 원자적으로 이벤트를 생성하거나 상태를 PROCESSING으로 업데이트
        ConsumedEvent consumedEvent = consumedEventRepository.upsertEventToProcessing(eventUuid, eventType, SagaStatus.PROCESSING);
        if (consumedEvent != null) {
            log.info("이벤트 처리 시작: eventUuid={}", eventUuid);
            return consumedEvent;
        } else {
            log.info("이미 처리 중이거나 완료된 이벤트: eventUuid={}", eventUuid);
            return null;
        }
    }

    public boolean updateStatus(String eventUuid, SagaStatus[] expectedStatus, SagaStatus newStatus, Boolean processed) {
        boolean updated = consumedEventRepository.updateSagaStatus(eventUuid, expectedStatus, newStatus, processed);
        if (!updated) {
            log.error("상태 업데이트 실패 : eventUuid={}, expectedStatus={}, newStatus={}", eventUuid, expectedStatus, newStatus);
        } else {
            log.info("상태 업데이트 성공 : eventUuid={}, newStatus={}", eventUuid, newStatus);
        }
        return updated;
    }
//    public ConsumedEvent handleIdempotencyAndUpdateStatus(String eventUuid, ConsumedEventType eventType) {
//        Optional<ConsumedEvent> optionalConsumedEvent = consumedEventRepository.findByProcessedFalseAndEventUuid(eventUuid);
//        if (optionalConsumedEvent.isPresent()) {
//            ConsumedEvent consumedEvent = optionalConsumedEvent.get();
//            // 1. 처리중이거나, 처리시도가 된적있는 이벤트
//            // 1-1 처리중인 이벤트
//            if (consumedEvent.getSagaStatus() != SagaStatus.FAILED) {
//                log.info("처리 중인 이벤트: eventUuid={}", eventUuid);
//                return null;
//            }
//            // 1-2 처리시도가 된적있는 이벤트
//            log.info("이번에 실패한 이벤트. 재처리 시도: eventUuid={}", eventUuid);
//            updateStatus(eventUuid, new SagaStatus[]{SagaStatus.FAILED}, SagaStatus.RETRYING, null);
//            return consumedEvent;
//        } else {
//            // 2. 한번도 시도된적없는 이벤트
//            log.info("새로운 객체 생성: eventUuid={}", eventUuid);
//            return createToEntity(eventUuid, eventType);
//        }
//    }
//
//    public ConsumedEvent createToEntity(String eventUuid, ConsumedEventType eventType) {
//        ConsumedEvent consumedEvent = consumedEventMapper.toEntity(eventUuid, eventType);
//        consumedEventRepository.save(consumedEvent);
//        return consumedEvent;
//    }
//
//    public boolean updateStatus(String eventUuid, SagaStatus[] expectedStatus, SagaStatus newStatus, Boolean processed) {
//        boolean updated = consumedEventRepository.updateSagaStatus(eventUuid, expectedStatus, newStatus, processed);
//        if (!updated) {
//            log.error("상태 업데이트 실패 : expectedStatus={}, newStatus={}", expectedStatus, newStatus);
//        } else {
//            log.info("상태 업데이트 성공 : expectedStatus={}, newStatus={}", expectedStatus, newStatus);
//        }
//        return updated;
//    }
}
