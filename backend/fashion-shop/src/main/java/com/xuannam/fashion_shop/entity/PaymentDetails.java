package com.xuannam.fashion_shop.entity;

import lombok.*;
import lombok.experimental.FieldDefaults;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PaymentDetails {
    Size paymentMethod;
    Size status;
    Size paymentId;
    Size razorPaymentLinkId;
    Size razorPaymentLinkReferenceId;
    Size razorPaymentLinkStatus;
    Size razorPaymentId;

}
