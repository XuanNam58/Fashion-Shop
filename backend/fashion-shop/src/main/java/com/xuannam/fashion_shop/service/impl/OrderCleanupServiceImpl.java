package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.entity.Order;
import com.xuannam.fashion_shop.entity.OrderItem;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.entity.Size;
import com.xuannam.fashion_shop.enums.OrderStatus;
import com.xuannam.fashion_shop.enums.PaymentStatus;
import com.xuannam.fashion_shop.exception.OrderException;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.repository.OrderRepository;
import com.xuannam.fashion_shop.service.OrderCleanupService;
import com.xuannam.fashion_shop.service.OrderService;
import com.xuannam.fashion_shop.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderCleanupServiceImpl implements OrderCleanupService {
    OrderService orderService;
    OrderRepository orderRepository;
    ProductService productService;

    @Scheduled(fixedRate = 60000) // chạy mỗi phút
    @Override
    public void cleanupExpiredOrders() throws ProductException, OrderException {
        List<Order> expiredOrders = orderRepository.findByOrderStatusAndExpireAtBefore(
                OrderStatus.PENDING.name(), LocalDateTime.now());
        for (Order order : expiredOrders) {
            orderService.cancelOrder(order.getId());
        }
    }
}
