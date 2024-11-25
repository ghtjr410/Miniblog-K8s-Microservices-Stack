package com.miniblog.viewcount.handler;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class EventHandlerRegistry {

    private final Map<Class<?>, EventHandler<?>> handlerMap = new HashMap<>();

    public void registerHandler(Class<?> eventType, EventHandler<?> handler) {
        handlerMap.put(eventType, handler);
    }

    @SuppressWarnings("unchecked")
    public <T> EventHandler<T> getHandler(Class<T> eventType) {
        return (EventHandler<T>) handlerMap.get(eventType);
    }
}
