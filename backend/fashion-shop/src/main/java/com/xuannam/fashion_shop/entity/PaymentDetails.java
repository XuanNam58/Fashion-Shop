package com.xuannam.fashion_shop.entity;

import jakarta.persistence.Entity;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetails {
    String paymentMethod;
    String status;
    String paymentId;
    String razorPaymentLinkId;
    String razorPaymentLinkReferenceId;
    String razorPaymentLinkStatus;
    String razorPaymentId;

}
