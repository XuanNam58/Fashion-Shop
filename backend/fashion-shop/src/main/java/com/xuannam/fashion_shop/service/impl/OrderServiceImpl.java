package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.entity.Address;
import com.xuannam.fashion_shop.entity.Order;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.OrderException;
import com.xuannam.fashion_shop.repository.CartRepository;
import com.xuannam.fashion_shop.service.CartService;
import com.xuannam.fashion_shop.service.OrderService;
import com.xuannam.fashion_shop.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    CartRepository cartRepository;
    CartService cartItemService;
    ProductService productService;

    @Override
    public Order createOrder(User user, Address shippingAddress) {

        return null;
    }

    @Override
    public List<Order> findOrderById(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return null;
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public Order canceledOrder(Long orderId) throws OrderException {
        return null;
    }

    @Override
    public List<Order> getAllOrders() {
        return null;
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {

    }
}
