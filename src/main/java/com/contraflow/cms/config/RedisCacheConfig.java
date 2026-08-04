package com.contraflow.cms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Stores cache values as JSON (instead of the default JDK serialization).
 * Benefits: cached DTOs do NOT need to implement Serializable, and Redis holds
 * human-readable JSON that you can inspect with redis-cli.
 */
@Configuration
public class RedisCacheConfig {

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))               // entries expire after 10 min
                .disableCachingNullValues()                     // don't cache nulls
                .serializeKeysWith(SerializationPair.fromSerializer(
                        new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer()));
    }
}
