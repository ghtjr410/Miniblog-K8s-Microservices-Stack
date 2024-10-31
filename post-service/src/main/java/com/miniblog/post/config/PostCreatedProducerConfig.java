package com.miniblog.post.config;

import com.miniblog.post.avro.PostCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

@Configuration
@RequiredArgsConstructor
public class PostCreatedProducerConfig {
    private final CommonProducerConfig commonProducerConfig;

    @Bean
    public ProducerFactory<String, PostCreatedEvent> postCreatedEventProducerFactory() {
        return new DefaultKafkaProducerFactory<>(commonProducerConfig.commonProducerFactory());
    }

    @Bean
    public KafkaTemplate<String, PostCreatedEvent> postCreatedEventKafkaTemplate() {
        return new KafkaTemplate<>(postCreatedEventProducerFactory());
    }
}
