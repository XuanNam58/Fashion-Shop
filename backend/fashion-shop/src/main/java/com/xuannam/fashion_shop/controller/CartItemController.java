package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.dto.response.ApiResponse;
import com.xuannam.fashion_shop.entity.CartItem;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.CartItemException;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.service.CartItemService;
import com.xuannam.fashion_shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart_items")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Cart Item Controller")
public class CartItemController {
    CartItemService cartItemService;
    UserService userService;

    @DeleteMapping("/{cartItemId}")
    @Operation(summary = "Delete cart item")
    public ResponseEntity<ApiResponse> deleteCartItem(@PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        cartItemService.removeCartItem(user.getId(), cartItemId);
        ApiResponse apiResponse = ApiResponse.builder().status(true).message("CartItem deleted successfully").build();
        return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }

    @PutMapping("/{cartItemId}")
    @Operation(summary = "Update cart item")
    public ResponseEntity<CartItem> updateCartItem(@RequestBody CartItem cartItem, @PathVariable Long cartItemId, @RequestHeader("Authorization") String jwt) throws UserException, CartItemException {
        User user = userService.findUserProfileByJwt(jwt);
        CartItem updatedCartItem = cartItemService.updateCartItem(user.getId(),cartItemId, cartItem);
        return new ResponseEntity<>(updatedCartItem, HttpStatus.OK);
    }

}
