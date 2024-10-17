package com.f4.fqs.queue.application.service;

import com.f4.fqs.commons.domain.exception.BusinessException;
import com.f4.fqs.commons.kafka_common.message.QueueCommand;
import com.f4.fqs.commons.reactive_kafka.kafka.producer.EventSourcingExecutor;
import com.f4.fqs.queue.application.response.AddQueueResponse;
import com.f4.fqs.queue.application.response.ConsumeQueueResponse;
import com.f4.fqs.queue.application.response.FindRankResponse;
import com.f4.fqs.queue.presentation.exception.QueueErrorCode;
import com.f4.fqs.queue.presentation.request.FindRankRequest;
import com.f4.fqs.queue.redis.service.RedisService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.CompletableFuture;

@Slf4j
@RequiredArgsConstructor
@Service
public class QueueService {

    private final RedisService redisService;
    private final EventSourcingExecutor executor;

    @Value("${spring.application.name}")
    private String SERVICE_NAME;


    public Mono<AddQueueResponse> lineUp() {

        /*return Mono.fromCallable(UUID::randomUUID)
                .flatMap(uuid -> {
                    Mono.fromRunnable(() -> redisService.lineUp(uuid))
                            .doOnError(e -> log.error("대기열 추가 요청에 실패했습니다. :: {}", uuid));

                    Mono.fromRunnable(() -> producer.send(CommonConstraints.EVENT,
                                    QueueCommand.addQueueCommand(SERVICE_NAME, uuid, LocalDateTime.now())))
                            .doOnError(e -> log.error("대기열 추가 이벤트 발행에 실패했습니다. :: {}", uuid));

                    return Mono.just(new AddQueueResponse(uuid.toString()));
                });*/

        UUID uuid = UUID.randomUUID();

        //1
//        CompletableFuture.runAsync(() -> redisService.lineUp(uuid));
        //2
        CompletableFuture.runAsync(() -> redisService.lineUp(uuid));

        CompletableFuture.runAsync(() -> executor.createEvent(QueueCommand.addQueueCommand(SERVICE_NAME, uuid, LocalDateTime.now())));


        return Mono.just(new AddQueueResponse(uuid.toString()));

    }

    public Mono<ConsumeQueueResponse> consume(int size) {

        if(size <= 0) {
            return Mono.error(new BusinessException(QueueErrorCode.CONSUME_SIZE_MUST_BE_OVER_ZERO));
        }

        List<String> list = redisService.consume(size);


        list.forEach(i -> CompletableFuture.runAsync(
                () -> executor.createEvent(QueueCommand.consumeQueueCommand(SERVICE_NAME, UUID.fromString(i), LocalDateTime.now())
                )
        ));
        /*return Mono.fromCallable(() -> redisService.consume(size))
                .flatMap(list -> {
                    list.stream()
                            .map(i -> producer.send(CommonConstraints.EVENT,
                                            QueueCommand.consumeQueueCommand(SERVICE_NAME, UUID.fromString(i), LocalDateTime.now())
                            ));

                    ConsumeQueueResponse result = new ConsumeQueueResponse(list);

                    return Mono.just(result);

                })
                .doOnError(e -> log.error("대기열 {}개 소모 중 문제가 발생했습니다.", size));*/
        return Mono.just(new ConsumeQueueResponse(list));

    }

    public Mono<FindRankResponse> getCurrentOrder(FindRankRequest request) {

        long myRank = redisService.getMyRank(request.userId().toString());

        return Mono.just(new FindRankResponse(myRank));
    }
}
