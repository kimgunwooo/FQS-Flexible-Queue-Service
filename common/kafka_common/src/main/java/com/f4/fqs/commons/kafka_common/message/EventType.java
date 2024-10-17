package com.f4.fqs.commons.kafka_common.message;

import lombok.Getter;

@Getter
public enum EventType {

    START_QUEUE,
    ADD_QUEUE,
    CONSUME_QUEUE,
    FINISH_WORK,
    END_QUEUE,
    ;

}
