package com.xuannam.fashion_shop.dto.response;

import lombok.*;
import lombok.experimental.FieldDefaults;
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AuthResponse {
    String jwt;
    String message;
}
