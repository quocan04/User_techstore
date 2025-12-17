package com.teamforone.tech_store.service.user;

import java.util.Map;

public interface VnPayService {
    /**
     * Tạo URL thanh toán VNPay
     */
    String createVnPayPaymentUrl(String orderId, long amount, String paymentMethod);

    /**
     * Xử lý kết quả thanh toán từ VNPay
     */
    boolean processVnPayPayment(Map<String, String> vnpParams);
}