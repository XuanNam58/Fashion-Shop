package com.xuannam.fashion_shop.repository;

import com.xuannam.fashion_shop.entity.Cart;
import com.xuannam.fashion_shop.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
