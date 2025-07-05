package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.dto.response.AverageRatingResponse;
import com.xuannam.fashion_shop.dto.response.RatingResponse;
import com.xuannam.fashion_shop.dto.resquest.RatingRequest;
import com.xuannam.fashion_shop.entity.Rating;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.exception.UserException;
import com.xuannam.fashion_shop.service.RatingService;
import com.xuannam.fashion_shop.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.hibernate.validator.constraints.ISBN;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ratings")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Rating Controller")
public class RatingController {
    RatingService ratingService;
    UserService userService;

    @PostMapping("/create")
    @Operation(summary = "Create rating")
    public ResponseEntity<Rating> createRating(@RequestBody RatingRequest request,
                                               @RequestHeader("Authorization") String jwt) throws UserException, ProductException {
        User user = userService.findUserProfileByJwt(jwt);
        Rating rating = ratingService.createRating(request, user);

        return new ResponseEntity<>(rating, HttpStatus.CREATED);
    }

    @GetMapping("/product/{productId}")
    @Operation(summary = "Get product ratings")
    public ResponseEntity<List<RatingResponse>> getProductRatings(@PathVariable Long productId) {
        List<RatingResponse> ratings = ratingService.getProductRatings(productId);

        return new ResponseEntity<>(ratings, HttpStatus.OK);
    }

    @GetMapping("/average/{productId}")
    @Operation(summary = "Get average rating")
    public ResponseEntity<AverageRatingResponse> getAverageRating(@PathVariable Long productId) {
        Double average = ratingService.getAverageRating(productId);
        Long total = ratingService.getTotalRatings(productId);

        return new ResponseEntity<>(new AverageRatingResponse(average, total), HttpStatus.OK);
    }

}
