package com.xuannam.fashion_shop.dto.resquest;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.xuannam.fashion_shop.entity.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.HashSet;
import java.util.Set;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CreateProductRequest {
    String title;
    String description;
    int price;
    int discountedPrice;
    int discountPercent;
    int quantity;
    String brand;
    String color;
    Set<Size> sizes = new HashSet<>();
    String imageUrl;
    String topLevelCategory;
    String secondLevelCategory;
    String thirdLevelCategory;
}
