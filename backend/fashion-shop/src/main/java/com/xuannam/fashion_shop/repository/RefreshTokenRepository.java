package com.xuannam.fashion_shop.repository;

import com.xuannam.fashion_shop.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
    Optional<RefreshToken> findByToken(String token);
    List<RefreshToken> findByEmailAndRevokedFalse(String email);
    void deleteByExpiryDateBefore(Instant expiryDate);
    void deleteByRevokedTrue();
}
