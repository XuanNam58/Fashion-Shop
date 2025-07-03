package com.xuannam.fashion_shop.service;

import com.xuannam.fashion_shop.dto.resquest.VNPayRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface VNPayService {
    String createPaymentUrl(VNPayRequest request, HttpServletRequest httpRequest);
    boolean handleCallback(HttpServletRequest request);
}
