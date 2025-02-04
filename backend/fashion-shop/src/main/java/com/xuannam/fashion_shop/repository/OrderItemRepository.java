package com.xuannam.fashion_shop.repository;

import com.xuannam.fashion_shop.entity.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}