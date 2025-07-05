package com.xuannam.fashion_shop.controller;

import com.xuannam.fashion_shop.dto.response.VNPayResponse;
import com.xuannam.fashion_shop.dto.resquest.VNPayRequest;
import com.xuannam.fashion_shop.service.OrderService;
import com.xuannam.fashion_shop.service.VNPayService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/payment")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
@RequiredArgsConstructor
@Tag(name = "Payment Controller", description = "Using VNPay sandbox to test payment")
public class PaymentController {
    VNPayService vnPayService;
    OrderService orderService;

    @PostMapping("/create")
    @Operation(summary = "Create payment", description = "User sends data and this API will use it to send to vnpay to process then return to vnpay-callback with status payment")
    public ResponseEntity<VNPayResponse> createPayment(@RequestBody VNPayRequest request, HttpServletRequest httpRequest) {
        String vnpayUrl = vnPayService.createPaymentUrl(request, httpRequest);
        return ResponseEntity.ok(new VNPayResponse(vnpayUrl));
    }

    @GetMapping("/vnpay-callback")
    @Operation(summary = "VNPay callback", description = "This API will redirect the status payment to frontend")
    public void vnpayCallback(HttpServletRequest request, HttpServletResponse response) throws IOException {
        boolean isSuccess = vnPayService.handleCallback(request);
        String redirectUrl = "http://localhost:3000/payment-result?status=" + (isSuccess ? "success" : "fail");
        response.sendRedirect(redirectUrl);
    }
}
