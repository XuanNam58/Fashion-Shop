package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.dto.response.ReviewResponse;
import com.xuannam.fashion_shop.dto.resquest.ReviewRequest;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.entity.Review;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.repository.ReviewRepository;
import com.xuannam.fashion_shop.service.ProductService;
import com.xuannam.fashion_shop.service.ReviewService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ReviewServiceImpl implements ReviewService {
    ReviewRepository reviewRepository;
    ProductService productService;

    @Override
    public Review createReview(ReviewRequest request, User user) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        Review review = Review.builder()
                .product(product)
                .user(user)
                .review(request.getReview())
                .createdAt(LocalDateTime.now())
                .build();
        return reviewRepository.save(review);
    }

    @Override
    public List<ReviewResponse> getAllReviews(Long productId) {
        List<Review> reviews = reviewRepository.getAllProductsReview(productId);
        return reviews.stream().map(review -> {
            Product product = review.getProduct();
            return ReviewResponse.builder()
                    .id(review.getId())
                    .review(review.getReview())
                    .productId(product.getId())
                    .user(review.getUser())
                    .createdAt(review.getCreatedAt())
                    .build();
        }).toList();
    }
}
