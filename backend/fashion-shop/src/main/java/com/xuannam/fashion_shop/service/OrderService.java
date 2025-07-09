package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.entity.Address;
import com.xuannam.fashion_shop.entity.Order;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.OrderException;
import com.xuannam.fashion_shop.exception.ProductException;

import java.util.List;

public interface OrderService {
    Order createOrder(User user, Address shippingAddress) throws ProductException;

    Order findOrderById(Long orderId) throws OrderException;

    List<Order> usersOrderHistory(Long userId);

    Order placedOrder(Long orderId) throws OrderException;

    Order confirmedOrder(Long orderId) throws OrderException;

    Order shippedOrder(Long orderId) throws OrderException;
    Order deliveredOrder(Long orderId) throws OrderException;
    Order cancelOrder(Long orderId) throws OrderException, ProductException;
    List<Order> getAllOrders();
    void deleteOrder(Long orderId) throws OrderException;

}
