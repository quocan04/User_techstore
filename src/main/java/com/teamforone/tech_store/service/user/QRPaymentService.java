package com.teamforone.tech_store.service.user;

import java.util.Map;

public interface QRPaymentService {
    String generateQRCodeUrl(String orderId, long amount);
    boolean checkPaymentStatus(String orderId);
    boolean confirmPayment(String orderId);
    Map<String, String> getBankInfo();
    String generateTransferContent(String orderId);
}