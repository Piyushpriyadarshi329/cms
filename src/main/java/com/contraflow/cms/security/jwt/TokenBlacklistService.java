package com.contraflow.cms.security.jwt;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class TokenBlacklistService {

    private static final Logger log = LoggerFactory.getLogger(TokenBlacklistService.class);
    private static final String PREFIX = "blacklist:jwt:";

    private final RedisTemplate<String, String> redisTemplate;

    /** Blacklist a token's jti. If Redis is down, log and continue (logout still succeeds client-side). */
    public void blacklistToken(String jti, long remainingTime) {
        try {
            redisTemplate.opsForValue().set(
                    PREFIX + jti,
                    "true",
                    remainingTime,
                    TimeUnit.MILLISECONDS
            );
        } catch (Exception redisDown) {
            log.warn("Redis unavailable - could not blacklist token: {}", redisDown.getMessage());
        }
    }

    /**
     * Fail OPEN: if Redis is unreachable, return false (treat as not blacklisted) so requests
     * aren't blocked. Revocation simply won't take effect while Redis is down.
     */
    public boolean isBlacklisted(String jti) {
        try {
            return Boolean.TRUE.equals(redisTemplate.hasKey(PREFIX + jti));
        } catch (Exception redisDown) {
            log.warn("Redis unavailable for blacklist check - ignoring: {}", redisDown.getMessage());
            return false;
        }
    }
}
