package com.f4.fqs.commons.kafka.config;


import com.f4.fqs.commons.kafka_common.message.QueueCommand;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.annotation.EnableKafka;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;

import java.util.Map;

@Configuration
@EnableKafka
public class KafkaConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String SERVER;


    @Bean
    public KafkaTemplate<String, QueueCommand> reactiveKafkaTemplate() {

        return new KafkaTemplate<>(
                        new DefaultKafkaProducerFactory<>(
                                Map.of(
                                    ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092",
                                    ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class,
                                    ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class,
                                    ProducerConfig.ACKS_CONFIG, "all"
                                )
                        )
                );
    }

}
