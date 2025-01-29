package com.xuannam.fashion_shop.exception;

import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;

public class OrderException extends Exception {
    public OrderException(String message) {
        super(message);
    }
}
