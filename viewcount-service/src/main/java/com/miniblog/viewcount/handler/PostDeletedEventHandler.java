package com.miniblog.viewcount.handler;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.viewcount.repository.ViewcountRepository;
import com.miniblog.viewcount.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostDeletedEventHandler implements EventConsumerHandler {
    private final ViewcountRepository viewcountRepository;

    @Override
    @Transactional
    public void handleEvent(UUID eventUuid, SpecificRecordBase event) {
        PostDeletedEvent postDeletedEvent = (PostDeletedEvent) event;
        viewcountRepository.deleteById(UUID.fromString(postDeletedEvent.getPostUuid().toString()));
    }

    @Override
    public ConsumedEventType getEventType(){
        return ConsumedEventType.POST_DELETE;
    }
}
