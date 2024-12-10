package com.miniblog.profile.mapper.consume;

import com.miniblog.profile.model.ConsumedEvent;
import com.miniblog.profile.util.ConsumedEventType;
import com.miniblog.profile.util.TracerUtility;
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