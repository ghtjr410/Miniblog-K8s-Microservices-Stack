package com.miniblog.like.handler.consume.post;

import com.miniblog.like.handler.consume.AbstractEventConsumerHandler;
import com.miniblog.like.repository.like.LikeRepository;
import com.miniblog.like.util.ConsumedEventType;
import com.miniblog.post.avro.PostDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostDeletedHandler extends AbstractEventConsumerHandler<PostDeletedEvent> {
    private final LikeRepository likeRepository;

    @Override
    public void processEvent(SpecificRecordBase event) {
        PostDeletedEvent postDeletedEvent = (PostDeletedEvent) event;
        UUID postUuid = UUID.fromString(postDeletedEvent.getPostUuid().toString());
        // 좋아요 삭제
        likeRepository.deleteByPostUuid(postUuid);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.POST_DELETE;
    }
}
