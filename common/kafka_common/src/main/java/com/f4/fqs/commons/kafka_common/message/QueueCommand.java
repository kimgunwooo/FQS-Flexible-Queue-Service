package com.f4.fqs.commons.kafka_common.message;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

@JsonTypeInfo(use = JsonTypeInfo.Id.NONE)
public record QueueCommand(
        String serviceName,
        UUID userId,
        EventType eventType,
        LocalDateTime createdAt
) implements Serializable {


    public static QueueCommand startQueueCommand(String serviceName, UUID userId, LocalDateTime createdAt) {
        return new QueueCommand(serviceName, userId, EventType.START_QUEUE, createdAt);
    }

    public static QueueCommand addQueueCommand(String serviceName, UUID userId, LocalDateTime createdAt) {
        return new QueueCommand(serviceName, userId, EventType.ADD_QUEUE, createdAt);
    }

    public static QueueCommand consumeQueueCommand(String serviceName, UUID userId, LocalDateTime createdAt) {
        return new QueueCommand(serviceName, userId, EventType.CONSUME_QUEUE, createdAt);
    }

    public static QueueCommand finishWorkCommand(String serviceName, UUID userId, LocalDateTime createdAt) {
        return new QueueCommand(serviceName, userId, EventType.FINISH_WORK, createdAt);
    }

    public static QueueCommand endQueueCommand(String serviceName, UUID userId, LocalDateTime createdAt) {
        return new QueueCommand(serviceName, userId, EventType.END_QUEUE, createdAt);
    }

}
