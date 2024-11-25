package com.miniblog.viewcount.service;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.viewcount.handler.EventHandler;
import com.miniblog.viewcount.handler.EventHandlerRegistry;
import com.miniblog.viewcount.model.ReceivedEvent;
import com.miniblog.viewcount.util.ReceivedEventType;
import com.miniblog.viewcount.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReceivedEventProcessor {
    private final ReceivedEventService receivedEventService;
    private final EventHandlerRegistry eventHandlerRegistry;

    public <T> void processEvent(UUID eventUuid, T event, Class<T> eventClass, ReceivedEventType eventType) {
        ReceivedEvent kafkaProcessedEvent = receivedEventService.handleIdempotencyAndUpdateStatus(eventUuid, eventType);
        // 이미 처리된 이벤트인 경우 처리 종료
        if(kafkaProcessedEvent == null) {
            return;
        }

        try {
            // 이벤트 핸들러 가져오기
            EventHandler<T> handler = eventHandlerRegistry.getHandler(eventClass);
            if (handler != null) {
                handler.handleEvent(eventUuid, event);
            } else {
                log.warn("해당 이벤트에 대한 핸들러가 없습니다.");
            }
            receivedEventService.updateStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.COMPLETED, true);
        } catch (Exception ex) {
            log.error("이벤트 처리 중 예외 발생: eventUuid={}, error={}", eventUuid, ex.getMessage());

            boolean statusUpdated = receivedEventService.updateStatus(eventUuid, new SagaStatus[]{SagaStatus.PROCESSING, SagaStatus.RETRYING}, SagaStatus.FAILED, null);
            if (!statusUpdated) {
                log.error("상태를 FAILED로 업데이트하는 데 실패했습니다: eventUuid={}", eventUuid);
            }
            throw  ex;
        }
    }
}
