package com.miniblog.viewcount.mapper.event;

import com.miniblog.viewcount.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapperFactory {
    private final ViewcountUpdatedEventMapper viewcountUpdatedEventMapper;

    public EventMapper getMapper(ProducedEventType eventType) {
        return switch (eventType) {
            case VIEWCOUNT_UPDATE -> viewcountUpdatedEventMapper;
            default -> throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
        };
    }
}
