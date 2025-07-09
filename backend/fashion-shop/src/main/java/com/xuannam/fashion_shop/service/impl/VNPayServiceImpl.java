package com.xuannam.fashion_shop.service.impl;

import com.xuannam.fashion_shop.dto.resquest.VNPayRequest;
import com.xuannam.fashion_shop.entity.*;
import com.xuannam.fashion_shop.enums.OrderStatus;
import com.xuannam.fashion_shop.enums.PaymentStatus;
import com.xuannam.fashion_shop.repository.OrderRepository;
import com.xuannam.fashion_shop.service.CartService;
import com.xuannam.fashion_shop.service.OrderService;
import com.xuannam.fashion_shop.service.ProductService;
import com.xuannam.fashion_shop.service.VNPayService;
import com.xuannam.fashion_shop.util.VNPayUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@Service
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VNPayServiceImpl implements VNPayService {
    @Value("${vnpay.tmn-code}")
    String vnp_TmnCode;
    @Value("${vnpay.secret-key}")
    String vnp_HashSecret;
    @Value("${vnpay.pay-url}")
    String vnp_Url;
    @Value("${vnpay.return-url}")
    String vnp_ReturnUrl;

    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;
    @Autowired
    ProductService productService;
    @Autowired
    CartService cartService;
    @Override
    public String createPaymentUrl(VNPayRequest request, HttpServletRequest httpRequest) {
        try {
            Order order = orderService.findOrderById(Long.valueOf(request.getOrderId()));

            String vnp_OrderInfo = "Thanh_toan_don_hang_" + request.getOrderId();
            String orderType = "other";
            String vnp_TxnRef = request.getOrderId(); // String.valueOf(System.currentTimeMillis());
            String vnp_IpAddr =  httpRequest.getRemoteAddr();
            String vnp_Amount = String.valueOf((long)(request.getAmount() * 100));
            String vnp_Locale = "vn";
            String vnp_BankCode = "NCB";

            Map<String, String> vnp_Params = new HashMap<>();
            vnp_Params.put("vnp_Version", "2.1.0");
            vnp_Params.put("vnp_Command", "pay");
            vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
            vnp_Params.put("vnp_Amount", vnp_Amount);
            vnp_Params.put("vnp_CurrCode", "VND");
            vnp_Params.put("vnp_BankCode", vnp_BankCode);
            vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
            vnp_Params.put("vnp_OrderInfo", vnp_OrderInfo);
            vnp_Params.put("vnp_OrderType", orderType);
            vnp_Params.put("vnp_Locale", vnp_Locale);
            vnp_Params.put("vnp_ReturnUrl", vnp_ReturnUrl);
            vnp_Params.put("vnp_IpAddr", vnp_IpAddr);
            vnp_Params.put("vnp_CreateDate", LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss")));


            List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
            Collections.sort(fieldNames);
            String vnp_SecureHash = VNPayUtil.hashAllFields(vnp_Params, vnp_HashSecret);
            // Xây dựng query string
            StringBuilder query = new StringBuilder();
            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                if (query.length() > 0) query.append('&');
                query.append(URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8));
                query.append('=');
                query.append(URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8));
            }
            query.append("&vnp_SecureHash=").append(URLEncoder.encode(vnp_SecureHash, StandardCharsets.UTF_8));

            // Update payment details
            PaymentDetails paymentDetails = order.getPaymentDetails();
            paymentDetails.setPaymentId(vnp_TxnRef);
            paymentDetails.setPaymentMethod("VNPAY");
            orderRepository.save(order);

            return vnp_Url + "?" + query.toString();
        } catch (Exception e) {
            throw new RuntimeException("VNPay create url error", e);
        }
    }

    @Override
    public boolean handleCallback(HttpServletRequest request) {
        Map<String, String> fields = new HashMap<>();
        for (Enumeration<String> params = request.getParameterNames(); params.hasMoreElements();) {
            String param = params.nextElement();
            if (param.startsWith("vnp_")) {
                fields.put(param, request.getParameter(param));
            }
        }
        String vnp_SecureHash = fields.remove("vnp_SecureHash");
        try {
            String signValue = VNPayUtil.hashAllFields(fields, vnp_HashSecret);

            if (signValue.equals(vnp_SecureHash)) {
                String vnp_TxnRef = fields.get("vnp_TxnRef");

                Order order = orderService.findOrderById(Long.valueOf(vnp_TxnRef));

                // Kiểm tra đơn hàng chưa hết hạn
                if (order.getExpireAt().isBefore(LocalDateTime.now())) {
                    orderService.cancelOrder(Long.valueOf(vnp_TxnRef)); // Hủy và khôi phục tồn kho
                    return false;
                }

                PaymentDetails paymentDetails = order.getPaymentDetails();
                paymentDetails.setVnpTransactionNo(fields.get("vnp_TransactionNo"));
                paymentDetails.setVnpResponseCode(fields.get("vnp_ResponseCode"));
                paymentDetails.setVnpAmount(fields.get("vnp_Amount"));
                paymentDetails.setVnpPayDate(fields.get("vnp_PayDate"));

                if ("00".equals(fields.get("vnp_ResponseCode"))) {
                    order.setOrderStatus(OrderStatus.PLACED.name());
                    paymentDetails.setStatus(PaymentStatus.COMPLETED.name());
                    cartService.clearCart(order.getUser().getId());
                } else {
                    // Thanh toán thất bại, hủy đơn và khôi phục tồn kho
                    orderService.cancelOrder(Long.valueOf(vnp_TxnRef));
                }
                orderRepository.save(order);
                return "00".equals(fields.get("vnp_ResponseCode"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
