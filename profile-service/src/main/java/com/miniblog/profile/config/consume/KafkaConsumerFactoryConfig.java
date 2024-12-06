package com.miniblog.profile.config.consume;

import lombok.RequiredArgsConstructor;
import org.apache.avro.specific.SpecificRecordBase;
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
public class KafkaConsumerFactoryConfig {
    private final ConsumerPropertiesConfig consumerPropertiesConfig;
    private final KafkaErrorHandlerConfig kafkaErrorHandlerConfig;

    @Bean
    public ConsumerFactory<String, SpecificRecordBase> consumerFactory() {
        return new DefaultKafkaConsumerFactory<>(consumerPropertiesConfig.consumerConfigs());
    }
    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, SpecificRecordBase> kafkaListenerContainerFactory() {
        ConcurrentKafkaListenerContainerFactory<String, SpecificRecordBase> factory = new ConcurrentKafkaListenerContainerFactory<>();
        factory.setConsumerFactory(consumerFactory());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);
        factory.setCommonErrorHandler(kafkaErrorHandlerConfig.kafkaErrorHandler());
        factory.getContainerProperties().setObservationEnabled(true);
        return factory;
    }
}
