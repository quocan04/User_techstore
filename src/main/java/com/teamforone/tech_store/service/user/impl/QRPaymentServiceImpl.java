package com.teamforone.tech_store.service.user.impl;

import com.teamforone.tech_store.model.Orders;
import com.teamforone.tech_store.repository.user.OrderRepository;
import com.teamforone.tech_store.service.user.QRPaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

@Service
public class QRPaymentServiceImpl implements QRPaymentService {

    // Thông tin ngân hàng của bạn
    private static final String BANK_ID = "970422"; // MB Bank
    private static final String ACCOUNT_NO = "0364424536";
    private static final String ACCOUNT_NAME = "TRAN TAN HAO";
    private static final String TEMPLATE = "compact2";

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public String generateQRCodeUrl(String orderId, long amount) {
        try {
            // Tạo nội dung chuyển khoản: TechStore + orderId
            String transferContent = "TechStore " + orderId;

            // URL encode các tham số
            String encodedName = URLEncoder.encode(ACCOUNT_NAME, StandardCharsets.UTF_8);
            String encodedContent = URLEncoder.encode(transferContent, StandardCharsets.UTF_8);

            // Tạo URL VietQR API
            return String.format(
                    "https://api.vietqr.io/image/%s-%s-%s.jpg?accountName=%s&amount=%d&addInfo=%s",
                    BANK_ID,
                    ACCOUNT_NO,
                    TEMPLATE,
                    encodedName,
                    amount,
                    encodedContent
            );

        } catch (Exception e) {
            throw new RuntimeException("Lỗi tạo QR Code URL: " + e.getMessage());
        }
    }

    @Override
    public boolean checkPaymentStatus(String orderId) {
        try {
            // Tìm đơn hàng trong database
            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + orderId));

            // KIỂM TRA FIELD STATUS CỦA MODEL HOrders
            // Nếu model có field 'status' hoặc 'orderStatus':
            // return "PAID".equalsIgnoreCase(order.getStatus());

            // HOẶC nếu có field riêng 'paymentStatus':
            // return "PAID".equalsIgnoreCase(order.getPaymentStatus());

            // TẠM THỜI: Luôn trả về false (chưa thanh toán)
            // BẠN CẦN SỬA LẠI DỰA VÀO FIELD THỰC TẾ CỦA MODEL
            return false;

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean confirmPayment(String orderId) {
        try {
            // Tìm đơn hàng
            Orders order = orderRepository.findById(orderId)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng: " + orderId));

            // CẬP NHẬT TRẠNG THÁI - SỬA LẠI DỰA VÀO MODEL CỦA BẠN
            // Nếu có field 'status':
            // order.setStatus("PAID");

            // Nếu có field 'paymentStatus':
            // order.setPaymentStatus("PAID");

            // Nếu có field 'updatedAt':
            // order.setUpdatedAt(new java.util.Date());

            // Lưu vào database
            orderRepository.save(order);

            return true;

        } catch (Exception e) {
            throw new RuntimeException("Lỗi xác nhận thanh toán: " + e.getMessage());
        }
    }

    @Override
    public Map<String, String> getBankInfo() {
        Map<String, String> bankInfo = new HashMap<>();
        bankInfo.put("bankId", BANK_ID);
        bankInfo.put("bankName", "MB Bank");
        bankInfo.put("accountNo", ACCOUNT_NO);
        bankInfo.put("accountName", ACCOUNT_NAME);
        return bankInfo;
    }

    public String generateTransferContent(String orderId) {
        return "TechStore " + orderId;
    }
}