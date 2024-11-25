package com.miniblog.viewcount.handler;

import java.util.UUID;

public interface EventHandler<T> {
    void handleEvent(UUID eventUuid, T event);

    Class<T> getEventType();
}
