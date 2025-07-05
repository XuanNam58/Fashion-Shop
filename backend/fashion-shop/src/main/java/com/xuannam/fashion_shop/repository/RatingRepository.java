package com.xuannam.fashion_shop.repository;

import com.xuannam.fashion_shop.entity.Rating;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RatingRepository extends JpaRepository<Rating, Long> {
    @Query("SELECT r FROM Rating r WHERE r.product.id = :productId")
    List<Rating> getAllProductRatings(@Param("productId") Long productId);
    @Query("SELECT AVG(r.rating) FROM Rating r WHERE r.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") Long productId);
    @Query("SELECT COUNT(r) FROM Rating r WHERE r.product.id = :productId")
    Long countByProductId(@Param("productId") Long ProductId);
}