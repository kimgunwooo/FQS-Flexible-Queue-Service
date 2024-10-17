package com.f4.fqs.commons.reactive_kafka.kafka.producer;

import com.f4.fqs.commons.kafka_common.message.QueueCommand;
import com.fasterxml.jackson.core.JsonProcessingException;

public interface ProducerService {

    void send(String topic, QueueCommand message) throws JsonProcessingException;

}
