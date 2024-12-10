package com.miniblog.comment.service.consume;

import com.miniblog.comment.handler.consume.EventConsumerHandler;
import com.miniblog.comment.handler.consume.EventConsumerHandlerRegistry;
import com.miniblog.comment.model.ConsumedEvent;
import com.miniblog.comment.util.ConsumedEventType;
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
            // 이벤트 검색/초기화
            ConsumedEvent consumedEvent = consumedEventService.findOrInitializeEvent(eventUuid, eventType);

            // 멱등성 (처리중인 이벤트, 처리된 이벤트)
            if(consumedEvent == null) {
                return;
            }

            // 이벤트 핸들러 가져오기
            EventConsumerHandler handler = handlerRegistry.getHandler(eventType);
            if (handler == null) {
                throw new IllegalArgumentException("Unsupported event type: " + eventType);
            }

            // 이벤트 처리 (비즈니스 로직)
            handler.handleEvent(event);

            // 완료 상태 업데이트
            consumedEventService.markEventAsCompleted(eventUuid);
        } catch (Exception ex) {
            log.error("이벤트 처리 중 예외 발생: eventUuid={}, error={}", eventUuid, ex.getMessage());
            // 실패 상태 업데이트
            consumedEventService.markEventAsFailed(eventUuid);
            throw  ex;
        }
    }
}
