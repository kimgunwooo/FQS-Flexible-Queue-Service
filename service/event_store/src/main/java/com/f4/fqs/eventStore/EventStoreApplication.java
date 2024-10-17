package com.f4.fqs.eventStore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(
		scanBasePackages = {
				"com.f4.fqs.eventStore",
				"com.f4.fqs.commons.domain",
				"com.f4.fqs.commons.kafka_common"
		}
)
public class EventStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(EventStoreApplication.class, args);
    }

}
