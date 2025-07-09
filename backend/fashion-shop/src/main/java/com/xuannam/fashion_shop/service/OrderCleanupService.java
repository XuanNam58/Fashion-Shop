package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.exception.OrderException;
import com.xuannam.fashion_shop.exception.ProductException;

public interface OrderCleanupService {
    void cleanupExpiredOrders() throws ProductException, OrderException;
}
