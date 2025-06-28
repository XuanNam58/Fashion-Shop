package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.entity.RefreshToken;
import com.xuannam.fashion_shop.repository.RefreshTokenRepository;
import com.xuannam.fashion_shop.service.RefreshTokenService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RefreshTokenServiceImpl implements RefreshTokenService {
    RefreshTokenRepository refreshTokenRepository;

    @Override
    public void saveRefreshToken(String token, String email) {
        long maxTokens = 5;
        List<RefreshToken> userTokens = refreshTokenRepository.findByEmailAndRevokedFalse(email);
        if (userTokens.size() >= maxTokens) {
            RefreshToken oldestToken = userTokens.get(0);
            refreshTokenRepository.delete(oldestToken);
        }

        refreshTokenRepository.save(RefreshToken.builder()
                .token(token)
                .email(email)
                .expiryDate(Instant.now().plusMillis(1000 * 60 * 60 * 24 * 7)) // 7d
                .revoked(false)
                .build());
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    @Override
    public void revokeToken(String token) {
        Optional<RefreshToken> refreshToken = findByToken(token);
        refreshToken.ifPresent(tokenEntity -> {
            tokenEntity.setRevoked(true);
            refreshTokenRepository.save(tokenEntity);
        });
    }

    @Override
    public boolean isTokenValid(String token) {
        Optional<RefreshToken> refreshToken = findByToken(token);
        return refreshToken.isPresent() && !refreshToken.get().isRevoked()
                && refreshToken.get().getExpiryDate().isAfter(Instant.now());
    }
}
