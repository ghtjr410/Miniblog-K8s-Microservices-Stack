package com.miniblog.like.handler.consume;

import com.miniblog.like.util.ConsumedEventType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventConsumerHandlerRegistry {
    private final Map<ConsumedEventType, EventConsumerHandler> handlerMap = new HashMap<>();

    public EventConsumerHandlerRegistry(List<EventConsumerHandler> handlers) {
        for (EventConsumerHandler handler: handlers) {
            handlerMap.put(handler.getEventType(), handler);
        }
    }

    public EventConsumerHandler getHandler(ConsumedEventType eventType) {
        return handlerMap.get(eventType);
    }
}
