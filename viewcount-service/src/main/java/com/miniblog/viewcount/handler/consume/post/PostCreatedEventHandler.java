package com.miniblog.viewcount.handler.consume.post;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.viewcount.handler.consume.EventConsumerHandler;
import com.miniblog.viewcount.model.Viewcount;
import com.miniblog.viewcount.repository.viewcount.ViewcountRepository;
import com.miniblog.viewcount.util.ConsumedEventType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.avro.specific.SpecificRecordBase;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostCreatedEventHandler implements EventConsumerHandler {

    private final ViewcountMapper viewcountMapper;
    private final ViewcountRepository viewcountRepository;

    @Override
    @Transactional
    public void handleEvent(SpecificRecordBase event) {
        PostCreatedEvent postCreatedEvent = (PostCreatedEvent) event;
        UUID postUuid = UUID.fromString(postCreatedEvent.getPostUuid().toString());
        Viewcount viewcount = viewcountMapper.createToEntity(UUID.fromString(postCreatedEvent.getPostUuid().toString()));
        try {
            viewcountRepository.save(viewcount);
        } catch (Exception ex) {
            log.error("알 수 없는 예외 발생: postUuid={}, error={}", postUuid, ex.getMessage());
            throw ex;
        }
    }

    @Override
    public ConsumedEventType getEventType() {
        return ConsumedEventType.POST_CREATE;
    }
}
