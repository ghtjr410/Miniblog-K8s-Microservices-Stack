package com.miniblog.viewcount.handler.consume.post;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.viewcount.handler.consume.AbstractEventConsumerHandler;
import com.miniblog.viewcount.repository.viewcount.ViewcountRepository;
import com.miniblog.viewcount.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostDeletedEventHandler extends AbstractEventConsumerHandler<PostDeletedEvent> {
    private final ViewcountRepository viewcountRepository;

    @Override
    protected void processEvent(SpecificRecordBase event) {
        PostDeletedEvent postDeletedEvent = (PostDeletedEvent) event;
        UUID postUuid = UUID.fromString(postDeletedEvent.getPostUuid().toString());
        viewcountRepository.deleteById(postUuid);
    }

    @Override
    public ConsumedEventType getEventType(){
        return ConsumedEventType.POST_DELETE;
    }
}
