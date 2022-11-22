package com.example.takehome.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory;
import org.springframework.data.redis.core.ReactiveRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfiguration {

    @Bean
    public ReactiveRedisTemplate<String, Long> reactiveRedisTemplate(ReactiveRedisConnectionFactory factory) {
        return new ReactiveRedisTemplate<String, Long>(
                factory,
                //TODO try and fix dis bs
                RedisSerializationContext.fromSerializer(new Jackson2JsonRedisSerializer(Long.class)));
    }
}