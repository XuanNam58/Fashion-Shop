package com.xuannam.fashion_shop.dto.resquest;

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
    java.lang.String title;
    java.lang.String description;
    int price;
    int discountedPrice;
    int discountPercent;
    int quantity;
    java.lang.String brand;
    java.lang.String color;
    Set<Size> sizes = new HashSet<>();
    java.lang.String imageUrl;
    java.lang.String topLevelCategory;
    java.lang.String secondLevelCategory;
    java.lang.String thirdLevelCategory;
}
