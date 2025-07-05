package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.entity.Address;
import com.xuannam.fashion_shop.entity.Order;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.OrderException;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.service.OrderService;
import com.xuannam.fashion_shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Order Controller")
public class OrderController {
    OrderService orderService;
    UserService userService;

    @PostMapping("/")
    @Operation(summary = "Create order")
    public ResponseEntity<Order> createOrder(@RequestBody Address shippingAddress,
                                             @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Order order = orderService.createOrder(user, shippingAddress);
        return new ResponseEntity<>(order, HttpStatus.CREATED);
    }

    @GetMapping("/user")
    @Operation(summary = "Get order history")
    public ResponseEntity<List<Order>> userOrderHistory(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        List<Order> orders = orderService.usersOrderHistory(user.getId());
        return new ResponseEntity<>(orders, HttpStatus.OK);
    }

    @GetMapping("/{orderId}")
    @Operation(summary = "Find order by id")
    public ResponseEntity<Order> findOrderById(@PathVariable Long orderId,
                                               @RequestHeader("Authorization") String jwt) throws UserException, OrderException {
        User user = userService.findUserProfileByJwt(jwt);

        Order order = orderService.findOrderById(orderId);
        return new ResponseEntity<>(order, HttpStatus.ACCEPTED);
    }

}
