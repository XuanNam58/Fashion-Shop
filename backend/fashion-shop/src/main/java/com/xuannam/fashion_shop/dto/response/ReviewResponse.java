package com.xuannam.fashion_shop.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.xuannam.fashion_shop.entity.Product;
import com.xuannam.fashion_shop.entity.User;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReviewResponse {
    Long id;
    String review;
    Long productId;
    User user;
    LocalDateTime createdAt;
}
