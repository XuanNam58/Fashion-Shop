package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.entity.Cart;
import com.xuannam.fashion_shop.entity.CartItem;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.CartItemException;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.repository.CartItemRepository;
import com.xuannam.fashion_shop.repository.CartRepository;
import com.xuannam.fashion_shop.service.CartItemService;
import com.xuannam.fashion_shop.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartItemServiceImpl implements CartItemService {
    CartItemRepository cartItemRepository;
    UserService userService;
    CartRepository cartRepository;

    @Override
    public CartItem createCartItem(CartItem cartItem) {
        cartItem.setQuantity(1);
        cartItem.setPrice(cartItem.getProduct().getPrice() * cartItem.getQuantity());
        cartItem.setDiscountedPrice(cartItem.getProduct().getDiscountedPrice() * cartItem.getQuantity());

        return cartItemRepository.save(cartItem);
    }

    @Override
    public CartItem updateCartItem(Long userId, Long id, CartItem cartItem) throws CartItemException, UserException {
        CartItem item = findCartItemById(id);
        User user = userService.findUserById(userId);

        if (user.getId().equals(userId)) {
            item.setQuantity(cartItem.getQuantity());
            item.setPrice(item.getQuantity() * item.getPrice());
            item.setDiscountedPrice(item.getQuantity() * item.getDiscountedPrice());
        }
        return cartItemRepository.save(item);
    }

    @Override
    public CartItem isCartItemExist(Cart cart, Product product, String size, Long userId) {
        return cartItemRepository.isCartItemExist(cart, product, size, userId);
    }

    @Override
    public void removeCartItem(Long userId, Long cartItemId) throws CartItemException, UserException {
        CartItem item = findCartItemById(cartItemId);
        User user = userService.findUserById(item.getUserId());
        User userReq = userService.findUserById(userId);

        if (user.getId().equals(userReq.getId())) {
            cartItemRepository.deleteById(cartItemId);
        } else {
            throw new UserException("You can't remove another user's item");

        }

    }

    @Override
    public CartItem findCartItemById(Long cartItemId) throws CartItemException {
        Optional<CartItem> opt = cartItemRepository.findById(cartItemId);
        if (opt.isPresent())
            return  opt.get();
        throw new CartItemException("CartItem not found with id: " + cartItemId);
    }
}
