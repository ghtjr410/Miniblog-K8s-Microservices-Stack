package com.miniblog.post.mapper.consume;

import com.miniblog.post.model.ConsumedEvent;
import com.miniblog.post.util.ConsumedEventType;
import com.miniblog.post.util.TracerUtility;
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
