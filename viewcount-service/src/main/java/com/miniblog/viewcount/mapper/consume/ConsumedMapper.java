package com.miniblog.viewcount.mapper.consume;

import com.miniblog.viewcount.model.ConsumedEvent;
import com.miniblog.viewcount.util.ConsumedEventType;
import com.miniblog.viewcount.util.TracerUtility;
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