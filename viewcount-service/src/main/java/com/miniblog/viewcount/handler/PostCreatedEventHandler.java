package com.miniblog.viewcount.handler;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.viewcount.mapper.ViewcountMapper;
import com.miniblog.viewcount.model.Viewcount;
import com.miniblog.viewcount.repository.ViewcountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostCreatedEventHandler implements EventHandler<PostCreatedEvent> {

    private final ViewcountMapper viewcountMapper;
    private final ViewcountRepository viewcountRepository;

    @Override
    @Transactional
    public void handleEvent(UUID eventUuid, PostCreatedEvent event) {
        Viewcount viewcount = viewcountMapper.createToEntity(UUID.fromString(event.getPostUuid().toString()));
        viewcountRepository.save(viewcount);
    }

    @Override
    public Class<PostCreatedEvent> getEventType() {
        return PostCreatedEvent.class;
    }
}
