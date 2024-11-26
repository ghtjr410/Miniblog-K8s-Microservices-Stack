package com.miniblog.viewcount.handler;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.viewcount.mapper.ViewcountMapper;
import com.miniblog.viewcount.model.Viewcount;
import com.miniblog.viewcount.repository.ViewcountRepository;
import com.miniblog.viewcount.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCreatedEventHandler implements EventConsumerHandler {

    private final ViewcountMapper viewcountMapper;
    private final ViewcountRepository viewcountRepository;

    @Override
    @Transactional
    public void handleEvent(UUID eventUuid, SpecificRecordBase event) {
        PostCreatedEvent postCreatedEvent = (PostCreatedEvent) event;
        Viewcount viewcount = viewcountMapper.createToEntity(UUID.fromString(postCreatedEvent.getPostUuid().toString()));
        viewcountRepository.save(viewcount);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.POST_CREATE;
    }
}
