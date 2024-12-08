package com.miniblog.profile.mapper.event;

import com.miniblog.profile.util.ProducedEventType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventMapperFactory {
    private final ProfileCreatedEventMapper profileCreatedEventMapper;
    private final ProfileUpdatedEventMapper profileUpdatedEventMapper;

    public EventMapper getMapper(ProducedEventType eventType) {
        return switch (eventType) {
            case PROFILE_CREATED -> profileCreatedEventMapper;
            case PROFILE_UPDATED -> profileUpdatedEventMapper;
            default ->  throw new IllegalArgumentException("지원하지 않는 이벤트 타입입니다: " + eventType);
        };
    }
}
