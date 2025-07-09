package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.dto.resquest.AddItemRequest;
import com.xuannam.fashion_shop.entity.*;
import com.xuannam.fashion_shop.exception.CartItemException;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.repository.CartRepository;
import com.xuannam.fashion_shop.repository.ProductRepository;
import com.xuannam.fashion_shop.service.CartItemService;
import com.xuannam.fashion_shop.service.CartService;
import com.xuannam.fashion_shop.service.ProductService;
import com.xuannam.fashion_shop.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class CartServiceImpl implements CartService {
    CartRepository cartRepository;
    CartItemService cartItemService;
    ProductRepository productRepository;
    UserService userService;

    @Override
    public Cart createCart(User user) {
        Cart cart = Cart.builder()
                .user(user)
                .cartItems(new HashSet<>())
                .build();
        return cartRepository.save(cart);
    }

    @Override
    public String addCartItem(Long userId, AddItemRequest request) throws ProductException, UserException, CartItemException {
        Cart cart = cartRepository.findByUserId(userId);

        if (cart == null) {
            cart = createCart(userService.findUserById(userId));
        }

        Optional<Product> productOptional = productRepository.findByIdWithLock(request.getProductId());
        if (productOptional.isEmpty()) {
            throw new ProductException("Product not found with id " + request.getProductId());
        }
        Product product = productOptional.get();

        Optional<Size> sizeOptional = product.getSizes().stream()
                .filter(size -> size.getName().equalsIgnoreCase(request.getSize()))
                .findFirst();

        if (sizeOptional.isEmpty()) {
            throw new ProductException("Size " + request.getSize() + " not available for product " + product.getTitle());
        }
        Size size = sizeOptional.get();
        if (size.getQuantity() < request.getQuantity()) {
            throw new ProductException("Insufficient stock for size " + request.getSize() + ". Available: " + size.getQuantity() + ", Requested: " + request.getQuantity());
        }

        CartItem isPresent = cartItemService.isCartItemExist(cart, product, request.getSize(), userId);

        if (isPresent == null) {
            CartItem item = CartItem.builder()
                    .product(product).cart(cart)
                    .quantity(request.getQuantity())
                    .price(request.getQuantity() * product.getPrice())
                    .discountedPrice(request.getQuantity() * product.getDiscountedPrice())
                    .size(request.getSize())
                    .userId(userId)
                    .build();
            CartItem createdCartItem = cartItemService.createCartItem(item);
            cart.getCartItems().add(createdCartItem);
        }
        else {
            // Nếu sản phẩm đã có trong giỏ, kiểm tra tổng số lượng
            int newQuantity = isPresent.getQuantity() + request.getQuantity();
            if (size.getQuantity() < newQuantity) {
                throw new ProductException("Insufficient stock for size " + request.getSize() + ". Available: " + size.getQuantity() + ", Requested: " + newQuantity);
            }
            isPresent.setQuantity(newQuantity);
            isPresent.setPrice(newQuantity * product.getPrice());
            isPresent.setDiscountedPrice(newQuantity * product.getDiscountedPrice());
            cartItemService.updateCartItem(userId, isPresent.getId(),isPresent);
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

    @Transactional
    @Override
    public void clearCart(Long userId) {
        cartRepository.deleteByUser_Id(userId);
    }
}
