package com.miniblog.query.service.consumedEvent;

import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.handler.EventConsumerHandlerRegistry;
import com.miniblog.query.model.ConsumedEvent;
import com.miniblog.query.util.ConsumedEventType;
import com.miniblog.query.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumedEventProcessor {
    private final ConsumedEventService consumedEventService;
    private final EventConsumerHandlerRegistry handlerRegistry;

    public void processEvent(String eventUuid, SpecificRecordBase event, ConsumedEventType eventType) {
        try {
            ConsumedEvent consumedEvent = consumedEventService.handleIdempotencyAndUpdateStatus(eventUuid, eventType);
            // 1. 멱등성 체크: 이미 처리 중이거나 완료된 이벤트는 null을 반환
            if (consumedEvent == null) {
                return;
            }
            // 2 이벤트 핸들러 가져오기
            EventConsumerHandler handler = handlerRegistry.getHandler(eventType);
            if (handler == null) {
                throw new IllegalArgumentException("Unsupported event type: " + eventType);
            }
            // 3 데이터 처리
            handler.handleEvent(event);
            // 4-1 상태 업데이트 (성공)
            consumedEventService.updateStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.COMPLETED, true);
        } catch (Exception ex) {
            log.error("이벤트 처리 중 예외 발생: eventUuid={}, error={}", eventUuid, ex.getMessage());
            // 4-2 상태 업데이트 (성공)
            consumedEventService.updateStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.FAILED, null);
            throw ex;
        }
    }
}
