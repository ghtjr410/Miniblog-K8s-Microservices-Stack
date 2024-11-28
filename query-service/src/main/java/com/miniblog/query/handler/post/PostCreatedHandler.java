package com.miniblog.query.handler.post;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.mapper.PostMapper;
import com.miniblog.query.model.Post;
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
public class PostCreatedHandler implements EventConsumerHandler {

    private final PostMapper postMapper;
    private final PostRepository postRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        PostCreatedEvent postCreatedEvent = (PostCreatedEvent) event;
        String postUuid = postCreatedEvent.getPostUuid().toString();

        if (postRepository.existsById(postUuid)) {
            log.info("이미 처리된 게시글 생성 이벤트: postUuid={}", postUuid);
            return;
        }

        Post post = postMapper.toCreatePost(postCreatedEvent);
        try {
            postRepository.save(post);
        } catch (DuplicateKeyException ex) {
            log.info("중복된 게시글 저장 시도: postUuid={}, error={}", postUuid, ex.getMessage());
            // 이미 저장된 경우 아무 작업도 하지 않음
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.POST_CREATE;
    }
}
