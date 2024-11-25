package com.miniblog.viewcount.config;

import com.miniblog.post.avro.PostCreatedEvent;
import com.miniblog.post.avro.PostDeletedEvent;
import com.miniblog.viewcount.handler.EventHandler;
import com.miniblog.viewcount.handler.EventHandlerRegistry;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class EventHandlerConfig {
    private final EventHandlerRegistry eventHandlerRegistry;
    private final List<EventHandler<?>> handlers;

    @PostConstruct
    public void init() {
        // 각 이벤트 타입과 핸들러를 매핑하여 등록
        for (EventHandler<?> handler: handlers) {
            eventHandlerRegistry.registerHandler(handler.getEventType(), handler);
        }
    }
}
