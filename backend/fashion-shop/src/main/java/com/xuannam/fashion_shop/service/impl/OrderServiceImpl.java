package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.entity.*;
import com.xuannam.fashion_shop.enums.OrderStatus;
import com.xuannam.fashion_shop.enums.PaymentStatus;
import com.xuannam.fashion_shop.exception.OrderException;
import com.xuannam.fashion_shop.repository.*;
import com.xuannam.fashion_shop.service.*;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    OrderItemRepository orderItemRepository;

    @Override
    public Order createOrder(User user, Address shippingAddress) {
        shippingAddress.setUser(user);
        Address address = addressRepository.save(shippingAddress);
        user.getAddresses().add(address);
        userRepository.save(user);

        Cart cart = cartService.findUserCart(user.getId());
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getCartItems()) {
            OrderItem orderItem = OrderItem.builder()
                    .price(item.getPrice())
                    .product(item.getProduct())
                    .quantity(item.getQuantity())
                    .size(item.getSize())
                    .userId(item.getUserId())
                    .discountedPrice(item.getDiscountedPrice())
                    .build();

            OrderItem createdOrderItem = orderItemRepository.save(orderItem);
            orderItems.add(createdOrderItem);
        }

        Order createdOrder = Order.builder()
                .user(user)
                .orderItems(orderItems)
                .totalPrice(cart.getTotalPrice())
                .totalDiscountedPrice(cart.getTotalDiscountedPrice())
                .discount(cart.getDiscount())
                .totalItem(cart.getTotalItem())
                .shippingAddress(shippingAddress)
                .orderStatus(OrderStatus.PENDING.name())
                .paymentDetails(PaymentDetails.builder()
                        .status(PaymentStatus.PENDING.name()).build())
                .expireAt(LocalDateTime.now().plusMinutes(15))
                .createdAt(LocalDateTime.now()).build();
        Order savedOrder = orderRepository.save(createdOrder);

        for (OrderItem item : orderItems) {
            item.setOrder(savedOrder);
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    @Override
    public Order findOrderById(Long orderId) throws OrderException {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isPresent())
            return order.get();
        throw new OrderException("Order not exist with id: " + orderId);
    }

    @Override
    public List<Order> usersOrderHistory(Long userId) {
        return orderRepository.getUserOrders(userId);
    }

    @Override
    public Order placedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.PLACED.name());
        order.getPaymentDetails().setStatus(PaymentStatus.COMPLETED.name());
        return order;
    }

    @Override
    public Order confirmedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.CONFIRMED.name());
        return orderRepository.save(order);
    }

    @Override
    public Order shippedOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.SHIPPED.name());
        return orderRepository.save(order);
    }

    @Override
    public Order deliveredOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.DELIVERED.name());
        return orderRepository.save(order);
    }

    @Override
    public Order cancelOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        order.setOrderStatus(OrderStatus.CANCELED.name());
        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public void deleteOrder(Long orderId) throws OrderException {
        Order order = findOrderById(orderId);
        orderRepository.deleteById(orderId);
    }
}
