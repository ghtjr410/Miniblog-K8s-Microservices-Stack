package com.miniblog.query.handler.like;

import com.miniblog.like.avro.LikeDeletedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.repository.like.LikeRepository;
import com.miniblog.query.repository.post.PostRepository;
import com.miniblog.query.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class LikeDeletedHandler implements EventConsumerHandler {
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        LikeDeletedEvent likeDeletedEvent = (LikeDeletedEvent) event;
        String postUuid = likeDeletedEvent.getPostUuid().toString();
        String likeUuid = likeDeletedEvent.getLikeUuid().toString();

        likeRepository.deleteById(likeUuid);

        if (!postRepository.existsById(postUuid)) {
            throw new IllegalArgumentException("Post with UUID " + postUuid + " does not exist.");
        }

        boolean updated = postRepository.decrementLikeCount(postUuid);
        if (!updated) {
            throw new IllegalArgumentException("Post not found for postUuid: " + postUuid);
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.LIKE_DELETE;
    }
}
