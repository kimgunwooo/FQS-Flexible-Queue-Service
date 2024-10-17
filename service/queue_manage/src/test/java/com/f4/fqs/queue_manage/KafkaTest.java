package com.f4.fqs.queue_manage;

import com.f4.fqs.commons.kafka.producer.EventSourcingExecutor;
import com.f4.fqs.queue_manage.application.service.KafkaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.UUID;

@SpringBootTest
public class KafkaTest {

    @Autowired
    private KafkaService kafkaService;

    @Test
    public void 메세지_전송_테스트() throws Exception {
    
        //given
        kafkaService.test();
        Thread.sleep(1000);

        //when
        
        //then
    
    }

}
