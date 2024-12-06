package com.miniblog.viewcount.handler.consume.post;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.viewcount.handler.consume.EventConsumerHandler;
import com.miniblog.viewcount.repository.viewcount.ViewcountRepository;
import com.miniblog.viewcount.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostDeletedEventHandler implements EventConsumerHandler {
    private final ViewcountRepository viewcountRepository;

    @Override
    @Transactional
    public void handleEvent(SpecificRecordBase event) {
        PostDeletedEvent postDeletedEvent = (PostDeletedEvent) event;
        UUID postUuid = UUID.fromString(postDeletedEvent.getPostUuid().toString());
        try {
            viewcountRepository.deleteById(postUuid);
        } catch (DataAccessException ex) {
            log.error("데이터 삭제 중 예외 발생: postUuid={}, error={}", postUuid, ex.getMessage());
            // 예외를 던져서 재시도가 이루어지도록 함
            throw ex;
        } catch (Exception ex) {
            log.error("알 수 없는 예외 발생: postUuid={}, error={}", postUuid, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ConsumedEventType getEventType(){
        return ConsumedEventType.POST_DELETE;
    }
}
