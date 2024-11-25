package com.miniblog.viewcount.service;

import com.miniblog.viewcount.mapper.ReceivedEventMapper;
import com.miniblog.viewcount.mapper.ViewcountMapper;
import com.miniblog.viewcount.model.ReceivedEvent;
import com.miniblog.viewcount.repository.ReceivedEventRepository;
import com.miniblog.viewcount.repository.ViewcountRepository;
import com.miniblog.viewcount.util.ReceivedEventType;
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
public class ReceivedEventService {
    private final ReceivedEventRepository receivedEventRepository;
    private final ReceivedEventMapper receivedEventMapper;
    private final ViewcountMapper viewcountMapper;
    private final ViewcountRepository viewcountRepository;

    public ReceivedEvent handleIdempotencyAndUpdateStatus(UUID eventUuid, ReceivedEventType eventType) {
        // 여기서 eventUuid를 기준으로 processed가 false인것을 가져와야한다
        Optional<ReceivedEvent> optionalReceivedEvent = receivedEventRepository.findByProcessedFalseAndEventUuid(eventUuid);
        if(optionalReceivedEvent.isPresent()){
            ReceivedEvent receivedEvent = optionalReceivedEvent.get();
            // 1. 처리중이거나 처리시도가된적있는 이벤트
            // 1-1 처리중인 이벤트
            if (receivedEvent.getSagaStatus() != SagaStatus.FAILED) {
                log.info("처리 중인 이벤트: eventUuid={}", eventUuid);
                return null;
            }
            // 1-2 처리시도가 된적있는 이벤트
            log.info("이번에 실패한 이벤트. 재처리 시도: eventUuid={}", eventUuid);
            updateStatus(eventUuid, new SagaStatus[]{SagaStatus.FAILED}, SagaStatus.RETRYING, null);
            return receivedEvent;

        } else {
            // 2. 한번도 시도된적없는 이벤트
            log.info("새로운 객체 생성: eventUuid={}", eventUuid);
            return createToEntity(eventUuid, eventType);
        }
    }


    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public ReceivedEvent createToEntity(UUID eventUuid) {
        ReceivedEvent receivedEvent = receivedEventMapper.toEntity(eventUuid);
        receivedEventRepository.save(receivedEvent);
        return receivedEvent;
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public boolean updateStatus(UUID eventUuid, SagaStatus[] expectedStatus, SagaStatus newStatus, Boolean processed) {
        boolean updated = receivedEventRepository.updateSagaStatus(eventUuid, expectedStatus, newStatus, processed);
        if (!updated) {
            log.info("상태 업데이트 실패");
        } else {
            log.info("상태 업데이트 성공");
        }
        return updated;
    }
}
