package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.service.TokenBlacklistService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class TokenBlacklistServiceImpl implements TokenBlacklistService {
    StringRedisTemplate redisTemplate;
    static String BLACKLIST_PREFIX = "jwt:blacklist:";
    @Override
    public void blacklistToken(String token, String reason, long ttlSeconds) {
        String key = BLACKLIST_PREFIX + token;
        redisTemplate.opsForHash().put(key, "reason", reason);
        redisTemplate.opsForHash().put(key, "blacklistedAt", Instant.now().toString());
        redisTemplate.expire(key, Duration.ofSeconds(ttlSeconds));
    }

    @Override
    public boolean isTokenBlacklisted(String token) {
        String key = BLACKLIST_PREFIX + token;
        return redisTemplate.hasKey(key);
    }
}
