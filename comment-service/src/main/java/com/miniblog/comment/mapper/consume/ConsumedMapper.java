package com.miniblog.comment.mapper.consume;

import com.miniblog.comment.model.ConsumedEvent;
import com.miniblog.comment.util.ConsumedEventType;
import com.miniblog.comment.util.TracerUtility;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ConsumedMapper {
    private final TracerUtility tracerUtility;

    public ConsumedEvent createToEntity(UUID eventUuid, ConsumedEventType eventType) {
        String traceId = tracerUtility.getTraceId();
        return ConsumedEvent.builder()
                .eventUuid(eventUuid)
                .traceId(traceId)
                .eventType(eventType)
                .build();
    }
}