package org.example.humans.global.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@EnableCaching
public class RedisConfig {
    @Value("${spring.data.redis.host}")
    private String host;

    @Value("${spring.data.redis.port}")
    private String port;

    @Bean
    public RedisConnectionFactory redisConnectionFactory(){
        return new LettuceConnectionFactory(host, Integer.parseInt(port));
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(connectionFactory); // Redis 연결 설정
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // Redis 키 직렬화 설정
        redisTemplate.setValueSerializer(new StringRedisSerializer()); // Redis 값 직렬화 설정
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, Object> redisBlackListTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        // JSON 직렬화 설정
        RedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        template.setValueSerializer(serializer);
        template.setKeySerializer(RedisSerializer.string());

        return template;
    }

}
