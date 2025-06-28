package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.repository.RefreshTokenRepository;
import com.xuannam.fashion_shop.service.TokenCleanupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class TokenCleanupServiceImpl implements TokenCleanupService {
    RefreshTokenRepository refreshTokenRepository;

    @Scheduled(cron = "0 0 0 * * ?") // Chạy hàng ngày lúc 0h
    @Override
    public void cleanupExpiredTokens() {
        refreshTokenRepository.deleteByExpiryDateBefore(Instant.now());
        refreshTokenRepository.deleteByRevokedTrue();
    }
}
