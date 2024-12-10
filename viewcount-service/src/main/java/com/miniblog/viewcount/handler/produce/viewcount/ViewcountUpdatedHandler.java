package com.miniblog.viewcount.handler.produce.viewcount;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.viewcount.avro.ViewcountUpdatedEvent;
import com.miniblog.viewcount.handler.produce.AbstractEventProducerHandler;
import com.miniblog.viewcount.producer.EventProducer;
import com.miniblog.viewcount.tracing.SpanFactory;
import com.miniblog.viewcount.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class ViewcountUpdatedHandler extends AbstractEventProducerHandler<ViewcountUpdatedEvent> {

    @Value("${viewcount.updated.event.topic.name}")
    private String viewcountUpdatedTopicName;

    public ViewcountUpdatedHandler(EventProducer eventProducer,
                                   ObjectMapper objectMapper,
                                   Tracer tracer,
                                   SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, ViewcountUpdatedEvent.class, ProducedEventType.VIEWCOUNT_UPDATE);
    }

    @PostConstruct
    public void init() {
        this.topicName = viewcountUpdatedTopicName;
    }
}
