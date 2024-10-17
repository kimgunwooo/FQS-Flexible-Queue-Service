package com.f4.fqs.queue;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(
		scanBasePackages = {
				"com.f4.fqs.queue",
				"com.f4.fqs.commons.domain",
				"com.f4.fqs.commons.reactive_kafka",
				"com.f4.fqs.commons.kafka_common",
		}
)
public class QueueApplication {

    public static void main(String[] args) {
        SpringApplication.run(QueueApplication.class, args);
    }

}
