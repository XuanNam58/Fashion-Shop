package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.entity.RefreshToken;

import java.util.Optional;

public interface RefreshTokenService {
    void saveRefreshToken(String token, String email);
    Optional<RefreshToken> findByToken(String token);
    void revokeToken(String token);
    boolean isTokenValid(String token);
}
