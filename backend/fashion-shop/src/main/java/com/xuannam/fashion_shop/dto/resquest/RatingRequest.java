package com.xuannam.fashion_shop.dto.resquest;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RatingRequest {
    Long productId;
    double rating;

}
