package com.xuannam.fashion_shop.repository;

import com.xuannam.fashion_shop.entity.Order;
import org.hibernate.validator.constraints.ru.INN;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {
    @Query("SELECT o FROM Order o WHERE o.user.id = :userId " +
            "AND (o.orderStatus = 'PLACED' OR o.orderStatus = 'CONFIRMED' " +
            "OR o.orderStatus = 'SHIPPED' OR o.orderStatus = 'DELIVERED')")
    List<Order> getUserOrders(@Param("userId") Long userId);
    List<Order> findByOrderStatusAndExpireAtBefore(String status, LocalDateTime expireAt);
}