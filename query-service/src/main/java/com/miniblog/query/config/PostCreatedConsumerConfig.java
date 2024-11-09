package com.miniblog.query.config;

import com.miniblog.post.avro.PostCreatedEvent;
import io.confluent.kafka.serializers.KafkaAvroDeserializerConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class PostCreatedConsumerConfig {
    private final CommonConsumerConfig commonConsumerConfig;
    private final PostCreatedErrorHandlerConfig postCreatedErrorHandlerConfig;

    @Bean
    public Map<String, Object> postCreatedEventConsumerConfigs() {
        return new HashMap<>(commonConsumerConfig.commonConsumerConfigs());
    }

    @Bean
    public ConsumerFactory<String, PostCreatedEvent> postCreatedEventConsumerFactory() {
        return new DefaultKafkaConsumerFactory<>(postCreatedEventConsumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, PostCreatedEvent> postCreatedEventListenerContainerFactory(){
        ConcurrentKafkaListenerContainerFactory<String, PostCreatedEvent> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(postCreatedEventConsumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(postCreatedErrorHandlerConfig.postCreatedEventErrorHandler());
        factory.getContainerProperties().setObservationEnabled(true); // Observation 활성화
        return factory;
    }
}
