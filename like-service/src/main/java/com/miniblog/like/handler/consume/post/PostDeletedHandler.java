package com.miniblog.like.handler.consume.post;

import com.miniblog.like.handler.consume.EventConsumerHandler;
import com.miniblog.like.repository.like.LikeRepository;
import com.miniblog.like.util.ConsumedEventType;
import com.miniblog.post.avro.PostDeletedEvent;
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
public class PostDeletedHandler implements EventConsumerHandler {
    private final LikeRepository likeRepository;

    @Override
    @Transactional
    public void handleEvent(SpecificRecordBase event) {
        PostDeletedEvent postDeletedEvent = (PostDeletedEvent) event;
        UUID postUuid = UUID.fromString(postDeletedEvent.getPostUuid().toString());
        try {
            // 좋아요 삭제
            likeRepository.deleteByPostUuid(postUuid);
        } catch (DataAccessException ex) {
            log.error("데이터 삭제 중 예외 발생: userUuid={}, error={}", postUuid, ex.getMessage());
            // 예외를 던져서 재시도가 이루어지도록 함
            throw ex;
        } catch (Exception ex) {
            log.error("알 수 없는 예외 발생: userUuid={}, error={}", postUuid, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.POST_DELETE;
    }
}
