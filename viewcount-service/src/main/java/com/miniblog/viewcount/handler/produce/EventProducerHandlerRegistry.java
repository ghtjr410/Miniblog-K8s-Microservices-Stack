package com.miniblog.viewcount.handler.produce;

import com.miniblog.viewcount.util.ProducedEventType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class EventProducerHandlerRegistry {

    private final Map<ProducedEventType, EventProducerHandler> handlerMap = new HashMap<>();

    public EventProducerHandlerRegistry(List<EventProducerHandler> handlers) {
        for (EventProducerHandler handler : handlers) {
            handlerMap.put(handler.getEventType(), handler);
        }
    }

    public EventProducerHandler getHandler(ProducedEventType publishedEventType) {
        return handlerMap.get(publishedEventType);
    }
}
