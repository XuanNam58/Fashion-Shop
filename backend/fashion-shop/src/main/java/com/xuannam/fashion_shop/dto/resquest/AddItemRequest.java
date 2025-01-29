package com.xuannam.fashion_shop.dto.resquest;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddItemRequest {
    Long productId;
    String size;
    int quantity;
    Integer price;
}
