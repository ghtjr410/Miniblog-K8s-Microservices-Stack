package com.miniblog.query.handler.post;

import com.miniblog.post.avro.PostUpdatedEvent;
import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.repository.post.PostRepository;
import com.miniblog.query.util.ConsumedEventType;
import com.miniblog.query.util.HtmlUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostUpdatedHandler implements EventConsumerHandler {
    private final PostRepository postRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        PostUpdatedEvent postUpdatedEvent = (PostUpdatedEvent) event;
        String postUuid = postUpdatedEvent.getPostUuid().toString();
        String title = postUpdatedEvent.getTitle().toString();
        String content = postUpdatedEvent.getContent().toString();
        String plainContent = HtmlUtils.toPlainText(content);

        Instant updatedDate = Instant.ofEpochMilli(postUpdatedEvent.getUpdatedDate());


        postRepository.updatePost(postUuid, title, plainContent, content, updatedDate);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.POST_UPDATE;
    }
}
