package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.dto.resquest.ReviewRequest;
import com.xuannam.fashion_shop.entity.Review;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
public class UserController {
    UserService userService;

    @GetMapping("/profile")
    public ResponseEntity<User> getUserProfileHandler(
            @RequestHeader("Authorization") String jwt) throws UserException {
        User user = userService.findUserProfileByJwt(jwt);

        return new ResponseEntity<>(user, HttpStatus.ACCEPTED);
    }
}
