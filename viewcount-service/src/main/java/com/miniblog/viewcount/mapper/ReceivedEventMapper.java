package com.miniblog.viewcount.mapper;

import com.miniblog.viewcount.model.ReceivedEvent;
import com.miniblog.viewcount.util.ReceivedEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class ReceivedEventMapper {
    public ReceivedEvent toEntity(UUID eventUuid, ReceivedEventType eventType) {
        return ReceivedEvent.builder()
                .eventUuid(eventUuid)
                .eventType(eventType)
                .build();
    }
}
