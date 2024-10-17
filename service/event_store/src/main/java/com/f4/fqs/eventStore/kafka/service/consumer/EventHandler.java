package com.f4.fqs.eventStore.kafka.service.consumer;

import com.f4.fqs.commons.kafka_common.message.QueueCommand;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class EventHandler {

    /**
     * 메세지 소모 없이 Kafka Stream 활용해서 데이터만 조회하기
     */

/*    @KafkaListener(
            topics = "event",
            groupId = "group_a")*/
    public void listen(QueueCommand message) {
        log.info("catched message :: {}", message);
    }

}
