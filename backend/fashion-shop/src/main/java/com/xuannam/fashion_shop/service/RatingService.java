package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.dto.response.RatingResponse;
import com.xuannam.fashion_shop.dto.resquest.RatingRequest;
import com.xuannam.fashion_shop.entity.Rating;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.ProductException;

import java.util.List;

public interface RatingService {
    Rating createRating(RatingRequest request, User user) throws ProductException;
    List<RatingResponse> getProductRatings(Long productId);
    Double getAverageRating(Long productId);
    Long getTotalRatings(Long productId);
}
