package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.entity.Cart;
import com.xuannam.fashion_shop.entity.CartItem;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.exception.CartItemException;
import com.xuannam.fashion_shop.exception.UserException;

public interface CartItemService {
    CartItem createCartItem(CartItem cartItem);
    CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException;
    CartItem isCartItemExist(Cart cart, Product product, String size, Long userId);
    void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException;
    CartItem findCartItemById(Long cartItemId) throws CartItemException;
}
