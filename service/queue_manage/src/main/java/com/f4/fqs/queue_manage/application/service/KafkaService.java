package com.f4.fqs.queue_manage.application.service;

import com.f4.fqs.commons.kafka.producer.EventSourcingExecutor;
import com.f4.fqs.commons.kafka_common.message.QueueCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaService {

    private final EventSourcingExecutor executor;

    public void test() {
        executor.createEvent(QueueCommand.addQueueCommand("test-service", UUID.randomUUID(), LocalDateTime.now()));
    }

}
