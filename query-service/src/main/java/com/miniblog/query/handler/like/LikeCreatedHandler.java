package com.miniblog.query.handler.like;

import com.miniblog.like.avro.LikeCreatedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.mapper.LikeMapper;
import com.miniblog.query.model.Like;
import com.miniblog.query.repository.like.LikeRepository;
import com.miniblog.query.repository.post.PostRepository;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeCreatedHandler implements EventConsumerHandler {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final LikeMapper likeMapper;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        LikeCreatedEvent likeCreatedEvent = (LikeCreatedEvent) event;
        String postUuid = likeCreatedEvent.getPostUuid().toString();
        String likeUuid = likeCreatedEvent.getLikeUuid().toString();

        if (likeRepository.existsById(likeUuid)) {
            log.info("이미 처리된 좋아요 생성 이벤트: likeUuid={}", likeUuid);
            return;
        }

        Like like = likeMapper.toCreateLike(likeCreatedEvent);
        try {
            likeRepository.save(like);
        } catch (DuplicateKeyException ex) {
            log.info("중복된 좋아요 저장 시도: likeUuid={}, error={}", likeUuid, ex.getMessage());
            // 이미 저장된 경우 아무 작업도 하지 않음
        }

        boolean updated = postRepository.incrementLikeCount(postUuid);
        if (!updated) {
            throw new IllegalArgumentException("Post not found for postUuid: " + postUuid);
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.LIKE_CREATE;
    }
}
