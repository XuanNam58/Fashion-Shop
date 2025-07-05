package com.xuannam.fashion_shop.repository;

import com.xuannam.fashion_shop.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    @Query("SELECT p FROM Product p " +
            "WHERE (p.category.name = :category OR :category='') " +
            "AND ((:minPrice IS NULL AND :maxPrice IS NULL) OR (p.discountedPrice BETWEEN :minPrice AND :maxPrice)) " +
            "AND (:minDiscount IS NULL OR p.discountPercent >= :minDiscount) " +
            "ORDER BY " +
            "CASE WHEN :sort = 'price_low' THEN p.discountedPrice END ASC, " +
            "CASE WHEN :sort = 'price_high' THEN p.discountedPrice END DESC")
    List<Product> filterProducts(@Param("category") String category,
                                 @Param("minPrice") Integer minPrice,
                                 @Param("maxPrice") Integer maxPrice,
                                 @Param("minDiscount") Integer minDiscount,
                                 @Param("sort") String sort);
    @Query("SELECT p FROM Product p WHERE p.category.name = :category")
    List<Product> findProductByCategory(String category);

    @Query("SELECT p FROM Product p " +
            "WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.color) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "ORDER BY p.numRatings DESC")
    List<Product> getProductSuggestions(@Param("q") String q, Pageable pageable);

    @Query("SELECT p FROM Product p " +
            "WHERE (LOWER(p.title) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.brand) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.color) LIKE LOWER(CONCAT('%', :q, '%')) " +
            "OR LOWER(p.category.name) LIKE LOWER(CONCAT('%', :q, '%'))) " +
            "ORDER BY p.createdAt DESC")
    Page<Product> searchProducts(@Param("q") String q, Pageable pageable);

}