package com.miniblog.viewcount.handler;

import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.viewcount.repository.ViewcountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostDeletedEventHandler implements EventHandler<PostDeletedEvent>{
    private final ViewcountRepository viewcountRepository;

    @Override
    @Transactional
    public void handleEvent(UUID eventUuid, PostDeletedEvent postDeletedEvent) {
        viewcountRepository.deleteById(UUID.fromString(postDeletedEvent.getPostUuid().toString()));
    }

    @Override
    public Class<PostDeletedEvent> getEventType(){
        return PostDeletedEvent.class;
    }
}
