package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.dto.response.RatingResponse;
import com.xuannam.fashion_shop.dto.resquest.RatingRequest;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.entity.Rating;
import com.xuannam.fashion_shop.entity.User;
import com.xuannam.fashion_shop.exception.ProductException;
import com.xuannam.fashion_shop.repository.RatingRepository;
import com.xuannam.fashion_shop.service.ProductService;
import com.xuannam.fashion_shop.service.RatingService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class RatingServiceImpl implements RatingService {
    RatingRepository ratingRepository;
    ProductService productService;

    @Override
    public Rating createRating(RatingRequest request, User user) throws ProductException {
        Product product = productService.findProductById(request.getProductId());
        Rating rating = Rating.builder()
                .product(product)
                .rating(request.getRating())
                .user(user)
                .createdAt(LocalDateTime.now())
                .build();
        return ratingRepository.save(rating);
    }

    @Override
    public List<RatingResponse> getProductRatings(Long productId) {
        List<Rating> ratings = ratingRepository.getAllProductRatings(productId);
        return ratings.stream().map(rating -> {
            Product product = rating.getProduct();
            return RatingResponse.builder()
                    .id(rating.getId())
                    .user(rating.getUser())
                    .productId(product.getId())
                    .rating(rating.getRating())
                    .createdAt(rating.getCreatedAt())
                    .build();
        }).toList();
    }

    @Override
    public Double getAverageRating(Long productId) {
        Double avg = ratingRepository.findAverageRatingByProductId(productId);
        return avg != null ? Math.round(avg * 10.0) / 10.0 : 0.0; // làm tròn 1 chữ số thập phân
    }

    @Override
    public Long getTotalRatings(Long productId) {
        return ratingRepository.countByProductId(productId);
    }
}
