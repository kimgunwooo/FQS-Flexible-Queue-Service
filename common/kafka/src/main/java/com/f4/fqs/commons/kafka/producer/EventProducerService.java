package com.f4.fqs.commons.kafka.producer;

import com.f4.fqs.commons.domain.exception.BusinessException;
import com.f4.fqs.commons.kafka_common.exception.StoreErrorCode;
import com.f4.fqs.commons.kafka_common.message.QueueCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Slf4j
@Service
@RequiredArgsConstructor
public final class EventProducerService implements ProducerService {

    private final KafkaTemplate<String, QueueCommand> producer;

    @Override
    public void send(String topic, QueueCommand message) {

        CompletableFuture.runAsync(() -> producer.send(topic, message))
                .exceptionally((e) -> {
                    throw new BusinessException(StoreErrorCode.INVALID_INPUT_VALUE);
                });
    }

}
