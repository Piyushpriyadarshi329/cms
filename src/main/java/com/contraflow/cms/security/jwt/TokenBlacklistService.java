package com.contraflow.cms.security.jwt;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private final RedisTemplate<String, String> redisTemplate;

    private static final String PREFIX = "blacklist:jwt:";

    public void blacklistToken(String token, long remainingTime) {

        redisTemplate.opsForValue().set(
                PREFIX + token,
                "true",
                remainingTime,
                TimeUnit.MILLISECONDS
        );
    }

    public boolean isBlacklisted(String token) {

        return Boolean.TRUE.equals(
                redisTemplate.hasKey(PREFIX + token)
        );
    }
}
