package com.miniblog.query.service;

import com.miniblog.query.model.ProcessedEvent;
import com.miniblog.query.repository.ProcessedEventRepository;
import com.miniblog.query.util.SagaStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class EventProcessingHelper {

    private final ProcessedEventRepository processedEventRepository;

    public ProcessedEvent handleIdempotencyAndUpdateStatus(String eventUuid) {
        Optional<ProcessedEvent> optionalProcessedEvent = processedEventRepository.findByEventUuid(eventUuid);
        if (optionalProcessedEvent.isPresent()) {
            ProcessedEvent processedEvent = optionalProcessedEvent.get();
            if (processedEvent.getSagaStatus() != SagaStatus.FAILED) {
                log.info("이미 처리된 이벤트입니다. eventUuid={}", eventUuid);
                return null; // 처리 종료를 알리기 위해 null 반환
            }
            log.info("이전에 실패한 이벤트입니다. 재처리를 시도합니다. eventUuid={}", eventUuid);
            updateSagaStatus(eventUuid, new SagaStatus[]{SagaStatus.FAILED}, SagaStatus.RETRYING);
            return processedEvent;
        } else {
            ProcessedEvent processedEvent = new ProcessedEvent();
            processedEvent.setEventUuid(eventUuid);
            processedEvent.setSagaStatus(SagaStatus.PROCESSING);
            processedEventRepository.save(processedEvent);
            return processedEvent;
        }
    }

    public void validateSagaStatus(ProcessedEvent processedEvent, String eventUuid) {
        if (processedEvent == null) {
            log.warn("ProcessedEvent가 null입니다. eventUuid={}", eventUuid);
            throw new IllegalStateException("ProcessedEvent가 null입니다.");
        }

        if (processedEvent.getSagaStatus() != SagaStatus.PROCESSING &&
                processedEvent.getSagaStatus() != SagaStatus.RETRYING) {
            log.warn("현재 sagaStatus가 PROCESSING이나 RETRYING 상태가 아닙니다. 처리 중단. eventUuid={}, currentStatus={}",
                    eventUuid, processedEvent.getSagaStatus());
            throw new IllegalStateException("현재 sagaStatus가 PROCESSING이나 RETRYING 상태가 아닙니다.");
        }
    }

    public void updateSagaStatus(String eventUuid, SagaStatus[] expectedStatuses, SagaStatus newStatus) {
        boolean updated = processedEventRepository.updateSagaStatus(eventUuid, expectedStatuses, newStatus);
        if (!updated) {
            log.warn("sagaStatus를 {}로 업데이트하지 못했습니다. eventUuid={}", newStatus, eventUuid);
            throw new IllegalStateException("sagaStatus를 " + newStatus + "로 업데이트하지 못했습니다.");
        }
    }
}
