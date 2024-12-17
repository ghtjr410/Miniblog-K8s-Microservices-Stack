package com.miniblog.comment.handler.consume;

import com.miniblog.comment.util.ConsumedEventType;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
public abstract class AbstractEventConsumerHandler<T extends SpecificRecordBase> implements EventConsumerHandler {

    @Override
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void handleEvent(SpecificRecordBase event) {
        try {
            processEvent(event);
        } catch (DataAccessException ex) {
            log.error("데이터 작업 중 예외 발생: error={}", ex.getMessage(), ex);
            throw ex; // 재시도를 위해 예외를 던짐
        } catch (Exception ex) {
            log.error("알 수 없는 예외 발생: error={}", ex.getMessage(), ex);
            throw new RuntimeException(ex);
        }
    }

    /**
     * 실제 비즈니스 로직을 구현하는 추상 메서드
     */
    protected abstract void processEvent(SpecificRecordBase event);

    @Override
    public abstract ConsumedEventType getEventType();
}
