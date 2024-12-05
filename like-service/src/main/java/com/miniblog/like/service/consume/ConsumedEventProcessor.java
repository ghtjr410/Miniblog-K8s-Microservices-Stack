package com.miniblog.like.service.consume;

import com.miniblog.like.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class ConsumedEventProcessor {

    public void processEvent(UUID eventUuid, SpecificRecordBase event, ConsumedEventType eventType) {
        // todo: comment에서 정해야돼 AccountDeleted는 아직 실험을 할 수가 없고 귀찮은 실험이라 Keep
        try {

            log.info("이벤트 처리 성공: eventUuid={}", eventUuid);
        } catch (Exception ex) {
            log.error("이벤트 처리 중 예외 발생: eventUuid={}, error={}", eventUuid, ex.getMessage());
            throw ex;
        }
    }
}
