package com.miniblog.query.consumer.viewcount;

import com.miniblog.query.consumer.GenericConsumer;
import com.miniblog.query.service.consumedEvent.ConsumedEventProcessor;
import com.miniblog.query.util.ConsumedEventType;
import com.miniblog.viewcount.avro.ViewcountUpdatedEvent;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ViewcountUpdatedConsumer extends GenericConsumer<ViewcountUpdatedEvent> {
    @Value("${viewcount.updated.event.topic.name}")
    private String topicName;

    public ViewcountUpdatedConsumer(ConsumedEventProcessor consumedEventProcessor) {
        super(consumedEventProcessor);
    }

    @Override
    protected ConsumedEventType getConsumedEventType() {
        return ConsumedEventType.VIEWCOUNT_UPDATE;
    }

    @Override
    public String topicName() {
        return topicName;
    }
}
