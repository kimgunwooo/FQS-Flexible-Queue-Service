package com.f4.fqs.queue_manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = {
				"com.f4.fqs.queue_manage",
				"com.f4.fqs.commons.domain",
				"com.f4.fqs.commons.kafka",
				"com.f4.fqs.commons.kafka_common"
		}
)
public class QueueManageApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueueManageApplication.class, args);
    }

}
