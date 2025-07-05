package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.configuration.JwtConstant;
import com.xuannam.fashion_shop.dto.response.ApiResponse;
import com.xuannam.fashion_shop.dto.resquest.AddItemRequest;
import com.xuannam.fashion_shop.entity.Cart;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.service.CartService;
import com.xuannam.fashion_shop.service.UserService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Cart Controller")
public class CartController {
    CartService cartService;
    UserService userService;

    @GetMapping("/")
    @Operation(summary = "Find user cart")
    public ResponseEntity<Cart> findUserCart(@RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);
        Cart cart = cartService.findUserCart(user.getId());
        return new ResponseEntity<>(cart, HttpStatus.OK);
    }

    @PutMapping("/add")
    @Operation(summary = "Add item to cart")
    public ResponseEntity<ApiResponse> addItemToCart(@RequestBody AddItemRequest request,
                                                     @RequestHeader("Authorization") String jwt) throws ProductException, UserException {
        User user = userService.findUserProfileByJwt(jwt);

        cartService.addCartItem(user.getId(), request);
        ApiResponse apiResponse = ApiResponse.builder()
                .status(true)
                .message("Item added to cart")
                .build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }


}
