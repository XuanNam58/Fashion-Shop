package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.entity.OrderItem;
import com.xuannam.fashion_shop.repository.OrderItemRepository;
import com.xuannam.fashion_shop.service.OrderItemService;
import com.xuannam.fashion_shop.service.OrderService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderItemServiceImpl implements OrderItemService {
    OrderItemRepository orderItemRepository;
    @Override
    public OrderItem createOrderItem(OrderItem orderItem) {
        return orderItemRepository.save(orderItem);
    }
}
