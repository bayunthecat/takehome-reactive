package com.example.takehome.service;

import com.example.takehome.exceptions.TooManyRequestsException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class LimitRequestsService {

    @Value("${spring.application.request.limit.unauthenticated}")
    private int limitPerUnauthenticated = 5;

    @Value("${spring.application.request.limit.authenticated}")
    private int limitPerAuthenticated = 20;

    @Value("${spring.application.request.limit.time}")
    private int time = 1;

    private final ReactiveRedisTemplate<String, Long> redisUserRequestCounter;

    public LimitRequestsService(ReactiveRedisTemplate<String, Long> redisUserRequestCounter) {
        this.redisUserRequestCounter = redisUserRequestCounter;
    }

    public Mono<String> checkRequestsExceedPerUser(String userKey) {
        return checkRequestsExceedByKey(userKey, limitPerAuthenticated);
    }

    public Mono<String> checkRequestsExceedPerIp(String ip) {
        return checkRequestsExceedByKey(ip, limitPerUnauthenticated);
    }

    private Mono<String> checkRequestsExceedByKey(String key, Integer limit) {
        return redisUserRequestCounter.opsForValue()
                .setIfAbsent(key, 0L, Duration.ofSeconds(time))
                .flatMap(result -> redisUserRequestCounter.opsForValue().increment(key))
                .flatMap(value -> {
                    if (value == 0 || value > limit) {
                        return Mono.error(new TooManyRequestsException(String.format("User %s exceeded %s requests per %s sec limit", key, value, time)));
                    }
                    return Mono.just(key);
                });
    }
}
