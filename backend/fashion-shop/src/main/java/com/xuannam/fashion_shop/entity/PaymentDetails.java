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
    String paymentMethod; // Ví dụ: "VNPAY"
    String status; // Ví dụ: "SUCCESS", "FAILED", "PENDING"
    String paymentId; // Có thể dùng để lưu vnp_TxnRef (mã giao dịch của đơn hàng)
    String vnpTransactionNo; // Mã giao dịch VNPAY (vnp_TransactionNo)
    String vnpResponseCode; // Mã phản hồi từ VNPAY
    String vnpAmount; // Số tiền giao dịch (đã nhân 100)
    String vnpPayDate; // Thời gian thanh toán

}
