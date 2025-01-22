package com.miniblog.query.handler.viewcount;

import com.miniblog.query.handler.EventConsumerHandler;
import com.miniblog.query.repository.post.PostRepository;
import com.miniblog.query.util.ConsumedEventType;
import com.miniblog.viewcount.avro.ViewcountUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class ViewcountUpdatedHandler implements EventConsumerHandler {
    private final PostRepository postRepository;

    @Override
    public void handleEvent(SpecificRecordBase event) {
        ViewcountUpdatedEvent viewcountUpdatedEvent = (ViewcountUpdatedEvent) event;
        String postUuid = viewcountUpdatedEvent.getPostUuid().toString();

        if (!postRepository.existsById(postUuid)) {
            throw new IllegalArgumentException("Post with UUID = " + postUuid + " does not exist.");
        }

        boolean updated = postRepository.incrementTotalViews(postUuid);
        if (!updated) {
            throw new IllegalArgumentException("Post not found for postUuid: " + postUuid);
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.VIEWCOUNT_UPDATE;
    }
}
