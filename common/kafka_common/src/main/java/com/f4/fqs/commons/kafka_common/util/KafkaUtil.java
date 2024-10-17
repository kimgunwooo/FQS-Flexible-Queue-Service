package com.f4.fqs.commons.kafka_common.util;

public final class KafkaUtil {
    private KafkaUtil() {}

    public static String makeEventTopicName(String topic) {
        return CommonConstraints.KAFKA_TOPIC_EVENT_PREFIX + topic;
    }

    public static String makeQueueTopicName(String topic) {
        return CommonConstraints.KAFKA_TOPIC_QUEUE_PREFIX + topic;
    }

}
