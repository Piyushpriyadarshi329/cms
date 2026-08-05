package com.contraflow.cms.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.Cache;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

/**
 * Stores cache values as JSON (instead of the default JDK serialization).
 * Benefits: cached DTOs do NOT need to implement Serializable, and Redis holds
 * human-readable JSON that you can inspect with redis-cli.
 *
 * Implements CachingConfigurer to provide a CacheErrorHandler so that if Redis is
 * unreachable (e.g. not provisioned on Railway), cache operations are logged and
 * IGNORED instead of throwing — the app simply falls back to the underlying method
 * (the DB). Caching becomes a best-effort optimization, never a hard dependency.
 */
@Configuration
public class RedisCacheConfig implements CachingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(RedisCacheConfig.class);

    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))               // entries expire after 10 min
                .disableCachingNullValues()                     // don't cache nulls
                .serializeKeysWith(SerializationPair.fromSerializer(
                        new StringRedisSerializer()))
                .serializeValuesWith(SerializationPair.fromSerializer(
                        GenericJacksonJsonRedisSerializer.builder().build()));
    }

    /**
     * Swallow Redis errors so a missing/unreachable Redis never breaks a request.
     * - GET error   -> treated as a cache miss (method runs against the DB)
     * - PUT/EVICT/CLEAR error -> logged and ignored (result still returned)
     */
    @Override
    public CacheErrorHandler errorHandler() {
        return new CacheErrorHandler() {
            @Override
            public void handleCacheGetError(RuntimeException ex, Cache cache, Object key) {
                log.warn("Redis GET failed for cache '{}' key '{}' - falling back to source. {}",
                        cache.getName(), key, ex.getMessage());
            }

            @Override
            public void handleCachePutError(RuntimeException ex, Cache cache, Object key, Object value) {
                log.warn("Redis PUT failed for cache '{}' key '{}' - result not cached. {}",
                        cache.getName(), key, ex.getMessage());
            }

            @Override
            public void handleCacheEvictError(RuntimeException ex, Cache cache, Object key) {
                log.warn("Redis EVICT failed for cache '{}' key '{}'. {}",
                        cache.getName(), key, ex.getMessage());
            }

            @Override
            public void handleCacheClearError(RuntimeException ex, Cache cache) {
                log.warn("Redis CLEAR failed for cache '{}'. {}", cache.getName(), ex.getMessage());
            }
        };
    }
}
