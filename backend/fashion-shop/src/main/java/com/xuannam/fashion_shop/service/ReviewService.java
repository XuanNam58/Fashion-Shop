package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.dto.response.ReviewResponse;
import com.xuannam.fashion_shop.dto.resquest.ReviewRequest;
import com.xuannam.fashion_shop.entity.Review;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.ProductException;

import java.util.List;

public interface ReviewService {
    Review createReview(ReviewRequest request, User user) throws ProductException;
    List<ReviewResponse> getAllReviews(Long productId);
}
