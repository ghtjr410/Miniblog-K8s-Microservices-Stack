package com.miniblog.query.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;

@EnableKafka
@Configuration
@RequiredArgsConstructor
public class KafkaConsumerFactoryConfig { // ConsumerFactory와 ConcurrentKafkaListenerContainerFactory를 생성
    private final ConsumerPropertiesConfig consumerPropertiesConfig;
    private final KafkaErrorHandlerConfig kafkaErrorHandlerConfig;

    @Bean
    public ConsumerFactory<String, Object> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerPropertiesConfig.consumerConfigs());
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, Object> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(kafkaErrorHandlerConfig.kafkaErrorHandler());
        factory.getContainerProperties().setObservationEnabled(true);
        return factory;
    }
}
