package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.entity.Order;
import com.xuannam.fashion_shop.enums.OrderStatus;
import com.xuannam.fashion_shop.enums.PaymentStatus;
import com.xuannam.fashion_shop.repository.OrderRepository;
import com.xuannam.fashion_shop.service.OrderCleanupService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@Slf4j
public class OrderCleanupServiceImpl implements OrderCleanupService {
    OrderRepository orderRepository;

    @Scheduled(fixedRate = 60000)
    @Override
    public void cleanupExpiredOrders() {
        List<Order> expiredOrders = orderRepository.findByOrderStatusAndExpireAtBefore(
                OrderStatus.PENDING.name(), LocalDateTime.now());
        for (Order order : expiredOrders) {
            order.setOrderStatus(OrderStatus.CANCELED.name());
            order.getPaymentDetails().setStatus(PaymentStatus.FAILED.name());
            orderRepository.save(order);
            log.info("Canceled expired order: {}", order.getId());
        }
    }
}
