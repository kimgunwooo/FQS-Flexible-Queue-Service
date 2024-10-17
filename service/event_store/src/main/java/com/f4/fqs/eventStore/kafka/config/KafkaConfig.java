package com.f4.fqs.eventStore.kafka.config;


import com.f4.fqs.commons.kafka_common.message.QueueCommand;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.support.serializer.JsonDeserializer;

import java.util.HashMap;
import java.util.Map;

@EnableKafka
@Configuration
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String SERVER;

    @Bean
    public ConsumerFactory<String, QueueCommand> consumerFactory() {

        Map<String, Object> configProps = new HashMap<>();
        configProps.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, SERVER);
        configProps.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, Boolean.TRUE);

        return new DefaultKafkaConsumerFactory<>(configProps, new StringDeserializer(), new JsonDeserializer<>(QueueCommand.class, false));
    }

    @Bean
    public ConcurrentKafkaListenerContainerFactory<String, QueueCommand> kafkaListenerContainerFactory() {

        ConcurrentKafkaListenerContainerFactory<String, QueueCommand> factory = new ConcurrentKafkaListenerContainerFactory<>();

        factory.setConsumerFactory(consumerFactory());

        factory.setContainerCustomizer(container -> {
            ContainerProperties properties = container.getContainerProperties();
            properties.setAckMode(ContainerProperties.AckMode.MANUAL);
        });

        return factory;

    }
/*
    @Bean
    public ReactiveKafkaConsumerTemplate<String, QueueCommand> reactiveKafkaConsumerTemplate(ConsumerFactory<String, QueueCommand> consumerFactory) {
        return new ReactiveKafkaConsumerTemplate<String, QueueCommand>();
    }*/

}
