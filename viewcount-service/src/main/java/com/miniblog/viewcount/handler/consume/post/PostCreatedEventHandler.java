package com.miniblog.viewcount.handler.consume.post;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.viewcount.handler.consume.AbstractEventConsumerHandler;
import com.miniblog.viewcount.mapper.viewcount.ViewcountMapper;
import com.miniblog.viewcount.model.Viewcount;
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
public class PostCreatedEventHandler extends AbstractEventConsumerHandler<PostCreatedEvent> {

    private final ViewcountMapper viewcountMapper;
    private final ViewcountRepository viewcountRepository;

    @Override
    public void processEvent(SpecificRecordBase event) {
        PostCreatedEvent postCreatedEvent = (PostCreatedEvent) event;
        UUID postUuid = UUID.fromString(postCreatedEvent.getPostUuid().toString());
        UUID userUuid = UUID.fromString(postCreatedEvent.getUserUuid().toString());
        Viewcount viewcount = viewcountMapper.createToEntity(postUuid, userUuid);
        viewcountRepository.save(viewcount);
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.POST_CREATE;
    }
}
