package com.f4.fqs.commons.reactive_kafka.kafka.producer;

import com.f4.fqs.commons.kafka_common.message.QueueCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public final class EventProducerService implements ProducerService {

    private final ReactiveKafkaProducerTemplate<String, QueueCommand> producer;

    @Override
    public void send(String topic, QueueCommand message) {

        producer.send(topic, message).doOnError(e -> {
            if (!Objects.isNull(e)) {
                log.error("이벤트 생성 에러 발생 :: {}", e.getMessage());
            }
        })
        .subscribe();
    }

}
