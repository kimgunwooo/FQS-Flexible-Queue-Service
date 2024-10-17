package com.f4.fqs.queue.infrastructure.repository;

import com.f4.fqs.queue.redis.service.RedisService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

@SpringBootTest
class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    public void 줄세우기_테스트() throws Exception {

        long start = System.currentTimeMillis();

        IntStream.range(0, 100000)
                .boxed()
                .forEach(i -> {
                    CompletableFuture.runAsync(() -> redisService.lineUp(UUID.randomUUID()));
                });
        System.out.println("SYNC FINISH :: " + (System.currentTimeMillis() - start));

        start = System.currentTimeMillis();

        Thread.sleep(3000);

        System.out.println("ASYNC FINISH :: " + (System.currentTimeMillis() - start));



    }

    @Test
    public void 대기열_소모() throws Exception {

        List<String> consumeList = redisService.consume(100);

        consumeList.forEach(o -> CompletableFuture.runAsync(() -> System.out.println(o)));


        //given

        //when

        //then

    }
    
}