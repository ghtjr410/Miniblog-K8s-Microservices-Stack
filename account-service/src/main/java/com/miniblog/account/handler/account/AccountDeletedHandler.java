package com.miniblog.account.handler.account;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.miniblog.account.avro.AccountDeletedEvent;
import com.miniblog.account.handler.AbstractEventProducerHandler;
import com.miniblog.account.producer.EventProducer;
import com.miniblog.account.tracing.SpanFactory;
import com.miniblog.account.util.ProducedEventType;
import io.micrometer.tracing.Tracer;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class AccountDeletedHandler extends AbstractEventProducerHandler<AccountDeletedEvent> {

    @Value("${account.deleted.event.topic.name}")
    private String accountDeletedTopicName;

    public AccountDeletedHandler(EventProducer eventProducer,
                                 ObjectMapper objectMapper,
                                 Tracer tracer,
                                 SpanFactory spanFactory) {
        super(eventProducer, objectMapper, tracer, spanFactory, AccountDeletedEvent.class, ProducedEventType.ACCOUNT_DELETE);
    }
    @PostConstruct
    public void init() {
        this.topicName = accountDeletedTopicName;
    }
}
