package com.xuannam.fashion_shop.repository;

import com.xuannam.fashion_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
}
