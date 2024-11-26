package com.miniblog.viewcount.mapper;

import com.miniblog.viewcount.model.ConsumedEvent;
import com.miniblog.viewcount.util.ConsumedEventType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class ConsumedEventMapper {
    public ConsumedEvent toEntity(UUID eventUuid, ConsumedEventType eventType) {
        return ConsumedEvent.builder()
                .eventUuid(eventUuid)
                .eventType(eventType)
                .build();
    }
}
