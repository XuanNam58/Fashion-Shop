package com.xuannam.fashion_shop.repository;

import com.xuannam.fashion_shop.entity.Cart;
import com.xuannam.fashion_shop.entity.CartItem;
import com.xuannam.fashion_shop.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    @Query("SELECT ci " +
            "FROM CartItem ci " +
            "WHERE ci.cart = :cart " +
            "AND ci.product = :product " +
            "AND ci.size = :size " +
            "AND ci.userId = :userId")
    CartItem isCartItemExist(@Param("cart") Cart cart,
                             @Param("product") Product product,
                             @Param("size") String size,
                             @Param("userId") Long userId);
}