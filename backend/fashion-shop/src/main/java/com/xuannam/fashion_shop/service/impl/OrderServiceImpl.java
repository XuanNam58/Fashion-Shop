package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.entity.*;
import com.xuannam.fashion_shop.exception.OrderException;
import com.xuannam.fashion_shop.repository.AddressRepository;
import com.xuannam.fashion_shop.repository.CartRepository;
import com.xuannam.fashion_shop.repository.OrderRepository;
import com.xuannam.fashion_shop.repository.UserRepository;
import com.xuannam.fashion_shop.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class OrderServiceImpl implements OrderService {
    CartRepository cartRepository;
    CartService cartService;
    CartItemService cartItemService;
    ProductService productService;
    AddressRepository addressRepository;
    UserRepository userRepository;
    OrderRepository orderRepository;
    OrderItemService orderItemService;

    @Override
    public Order createOrder(User user, Address shippingAddress) {
        shippingAddress.setUser(user);
        Address address = addressRepository.save(shippingAddress);
        user.getAddresses().add(address);
        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item:cart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
        }

        return null;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
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
    public Order cancelOrder(Long orderId) throws OrderException {
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
