package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.dto.resquest.AddItemRequest;
import com.xuannam.fashion_shop.entity.Cart;
import com.xuannam.fashion_shop.entity.CartItem;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.repository.CartRepository;
import com.xuannam.fashion_shop.service.CartItemService;
import com.xuannam.fashion_shop.service.CartService;
import com.xuannam.fashion_shop.service.ProductService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    CartItemService cartItemService;
    ProductService productService;

    @Override
    public Cart createCart(User user) {
        Cart cart = Cart.builder().user(user).build();
        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest request) throws ProductException {
        Cart cart = cartRepository.findByUserId(userId);
        Product product = productService.findProductById(request.getProductId());
        CartItem isPresent = cartItemService.isCartItemExist(cart, product, request.getSize(), userId);

        if (isPresent == null) {
            CartItem item = CartItem.builder().product(product).cart(cart).quantity(request.getQuantity()).price(request.getQuantity() * product.getDiscountedPrice()).size(request.getSize()).userId(userId).build();
            CartItem createdCartItem = cartItemService.createCartItem(item);
            cart.getCartItems().add(createdCartItem);
        }

        return "Item Add To Cart";
    }

    @Override
    public Cart findUserCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId);

        int totalPrice = 0;
        int totalDiscountedPrice = 0;
        int totalItem = 0;

        for (CartItem cartItem : cart.getCartItems()) {
            totalPrice += cartItem.getPrice();
            totalDiscountedPrice += cartItem.getDiscountedPrice();
            totalItem += cartItem.getQuantity();
        }

        cart.setTotalPrice(totalPrice);
        cart.setTotalDiscountedPrice(totalDiscountedPrice);
        cart.setTotalItem(totalItem);
        cart.setDiscount(totalPrice - totalDiscountedPrice);

        return cartRepository.save(cart);
    }
}
