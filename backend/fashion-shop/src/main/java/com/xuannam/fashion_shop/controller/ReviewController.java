package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.dto.response.ReviewResponse;
import com.xuannam.fashion_shop.dto.resquest.RatingRequest;
import com.xuannam.fashion_shop.dto.resquest.ReviewRequest;
import com.xuannam.fashion_shop.entity.Rating;
import com.xuannam.fashion_shop.entity.Review;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.service.ReviewService;
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
@RequestMapping("/api/reviews")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Review Controller")
public class ReviewController {
    ReviewService reviewService;
    UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Create review")
    public ResponseEntity<Review> createReview(@RequestBody ReviewRequest request,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Review review = reviewService.createReview(request, user);

        return new ResponseEntity<>(review, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get product reviews")
    public ResponseEntity<List<ReviewResponse>> getProductReviews(@PathVariable Long productId) {
        List<ReviewResponse> reviews = reviewService.getAllReviews(productId);

        return new ResponseEntity<>(reviews, HttpStatus.OK);
    }

}
