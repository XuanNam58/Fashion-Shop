package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.dto.resquest.AddItemRequest;
import com.xuannam.fashion_shop.entity.Cart;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.CartItemException;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.exception.UserException;

public interface CartService {
    Cart createCart(User user);
    String addCartItem(Long userId, AddItemRequest request) throws ProductException, UserException, CartItemException;
    Cart findUserCart(Long userId);
    void clearCart(Long userId);
}
