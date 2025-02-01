package com.xuannam.fashion_shop.entity;

import jakarta.persistence.Column;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentInformation {
    @Column(name = "cardholder_name")
    Size cardHolderName;
    @Column(name = "card_number")
    Size cardNumber;
    @Column(name = "expiration_date")
    LocalDate expirationDate;
    @Column(name = "cvv")
    Size cvv;

}
