package com.xuannam.fashion_shop.service;

public interface TokenBlacklistService {
    void blacklistToken(String token, String reason, long ttlSeconds);
    boolean isTokenBlacklisted(String token);
}
