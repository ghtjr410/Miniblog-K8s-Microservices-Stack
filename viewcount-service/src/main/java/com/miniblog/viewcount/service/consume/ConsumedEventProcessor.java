package com.miniblog.viewcount.service.consume;

import com.miniblog.viewcount.handler.consume.EventConsumerHandler;
import com.miniblog.viewcount.handler.consume.EventConsumerHandlerRegistry;
import com.miniblog.viewcount.model.ConsumedEvent;
import com.miniblog.viewcount.util.ConsumedEventType;
import com.miniblog.viewcount.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumedEventProcessor {
    private final ConsumedEventService consumedEventService;
    private final EventConsumerHandlerRegistry handlerRegistry;

    public void processEvent(UUID eventUuid, SpecificRecordBase event, ConsumedEventType eventType) {
        try {
            ConsumedEvent consumedEvent = consumedEventService.handleIdempotencyAndUpdateStatus(eventUuid, eventType);
            // 이미 처리된 이벤트인 경우 처리 종료
            if(consumedEvent == null) {
                return;
            }

            // 이벤트 핸들러 가져오기
            EventConsumerHandler handler = handlerRegistry.getHandler(eventType);
            if (handler == null) {
                throw new IllegalArgumentException("Unsupported event type: " + eventType);
            }
            handler.handleEvent(event);
            consumedEventService.updateStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.COMPLETED, true);
        } catch (Exception ex) {
            log.error("이벤트 처리 중 예외 발생: eventUuid={}, error={}", eventUuid, ex.getMessage());

            boolean statusUpdated = consumedEventService.updateStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.FAILED, null);
            if (!statusUpdated) {
                log.error("상태를 FAILED로 업데이트하는 데 실패했습니다: eventUuid={}", eventUuid);
            }
            throw  ex;
        }
    }
}
