package com.f4.fqs.queue.redis.service;

import com.f4.fqs.commons.domain.exception.BusinessException;
import com.f4.fqs.commons.domain.util.ParsingUtil;
import com.f4.fqs.queue.presentation.exception.QueueErrorCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.concurrent.TimeUnit;

import static com.f4.fqs.commons.domain.util.CommonConstraints.QUEUE_NAME;


@Service
@RequiredArgsConstructor
@Slf4j
public class RedisService {

    private final RedisTemplate<String, String> redisTemplate;

    // 값을 redis에 저장하는 메서드
    public void setValue(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 값과 함께 TTL(만료시간)을 설정해서 저장하는 메서드
    public void setValueWithExpiry(String key, String value, long timeout, TimeUnit unit) {
        redisTemplate.opsForValue().set(key, value, timeout, unit);
    }

    // redis에서 값을 가져오는 메서드
    public Object getValue(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    // redis에서 값을 가져오는 메서드
    public <T> T getValueAsClass(String key, Class<T> clazz) {
        return ParsingUtil.parseJsonString(redisTemplate.opsForValue().get(key), clazz);
    }

    // redis에서 특정 키의 TTL(남은 만료시간)을 조회하는 메서드
    public Long getExpire(String key) {
        return redisTemplate.getExpire(key);
    }

    // 특정 키의 만료시간을 갱신하는 메서드,
    public Boolean setExpire(String key, long timeout, TimeUnit unit) {
        return redisTemplate.expire(key, timeout, unit);
    }

    // redis에 키가 존재하는지 확인하는 메서드
    public Boolean hasKey(String key) {
        return redisTemplate.hasKey(key);
    }

    public void lineUp(UUID userId) {

        LocalDateTime now = LocalDateTime.now();
        redisTemplate.opsForZSet()
                .add(QUEUE_NAME, userId.toString(), now.toEpochSecond(ZoneOffset.UTC) + (now.getNano() / 1000000000.0));

    }

    public List<String> consume(int size) {

        List<String> result = Objects.requireNonNull(
                    redisTemplate.opsForZSet().range(QUEUE_NAME, 0, size - 1)
                )
                .stream()
//                .map(i -> {
//                    try {
//                        return ParsingUtil.readJsonString(i, Map.class);
//                    } catch (RuntimeException e) {
//                        return ParsingUtil.parseText(i);
//                    }
//                })
//                .map(i -> Map.of("userId", i))
                .toList();

        redisTemplate.opsForZSet().removeRange(QUEUE_NAME, 0, size - 1);

        return result;

    }

    public long getMyRank(String userId) {

        System.out.println("userId = " + userId);

        Long rank = Optional.ofNullable(
                redisTemplate.opsForZSet().rank(QUEUE_NAME, userId)
        ).orElseThrow(() -> new BusinessException(QueueErrorCode.NOT_EXIST_WAITING_INFO));

        return rank;
    }

}