package com.teamforone.tech_store.controller.user;

// --- CÁC IMPORT CẦN THIẾT ---
// DTOs (từ package dto.user)

import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.dto.response.Response2;
import com.teamforone.tech_store.dto.response.VnPayPaymentResponse;
import com.teamforone.tech_store.model.Orders;
import com.teamforone.tech_store.service.user.OrderService;
import com.teamforone.tech_store.service.user.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.Map;
// --- KẾT THÚC IMPORT ---


@RestController
@RequestMapping("/api/user/checkout")
public class CheckoutController {

    private final OrderService orderService; // Lỗi 'Could not autowire' ở đây sẽ được sửa sau
    private final VnPayService vnPayService; // Lỗi 'Could not autowire' ở đây sẽ được sửa sau

    @Autowired
    public CheckoutController(OrderService orderService, VnPayService vnPayService) {
        this.orderService = orderService;
        this.vnPayService = vnPayService;
    }


    // --- Helper để lấy ID User hiện tại ---
    private String getCurrentUserId() {
        // **QUAN TRỌNG:** Bạn cần thay thế logic này
        // bằng cách lấy ID (String) của User đang đăng nhập.
        return "user_test_id"; // Thay bằng logic lấy ID User thực tế
    }

    /**
     * POST /api/user/checkout/vnpay
     * Bước 1: Tạo đơn hàng từ giỏ hàng và Khởi tạo giao dịch VNPAY.
     */
    @PostMapping("/vnpay")
    public ResponseEntity<?> createOrderAndVnPayPayment(@RequestBody CheckoutRequest request) {
        try {
            String userId = getCurrentUserId();

            // 1. Tạo đơn hàng (sẽ gọi OrderService)
            Orders newOrder = orderService.createOrderFromCart(userId, request);

            // 2. Tạo URL thanh toán VNPAY
            // VNPAY yêu cầu số tiền là Số nguyên (long) và nhân 100
            long amountVnPay = newOrder.getTotalAmount().multiply(new BigDecimal(100)).longValue();

            String paymentUrl = vnPayService.createVnPayPaymentUrl(
                    newOrder.getOrderId(), // Dùng OrderId làm mã tham chiếu (vnp_TxnRef)
                    amountVnPay,
                    request.getPaymentMethod() // "VNPAY"
            );

            // 3. Trả về URL VNPAY cho client
            VnPayPaymentResponse response = new VnPayPaymentResponse("success", "Tạo link VNPAY thành công", paymentUrl);
            return ResponseEntity.ok(response);

        } catch (RuntimeException e) {
            Response2<?> response = new Response2<>("error", e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /api/user/checkout/vnpay-return
     * Bước 2: Xử lý kết quả VNPAY trả về (khi user thanh toán xong)
     */
    @GetMapping("/vnpay-return")
    public RedirectView processVnPayReturn(@RequestParam Map<String, String> vnp_Params) {

        // 1. Xử lý kết quả VNPAY (sẽ gọi VnPayService)
        boolean isSuccess = vnPayService.processVnPayPayment(vnp_Params);
        String orderId = vnp_Params.get("vnp_TxnRef");

        // URL frontend (thay đổi nếu cần)
        String frontendBaseUrl = "http://localhost:3000"; // Hoặc URL React/Vue của bạn

        if (isSuccess) {
            // Cập nhật trạng thái đơn hàng sang PAID (sẽ gọi OrderService)
            orderService.updateOrderStatus(orderId, "PAID");

            // Chuyển hướng về trang thông báo thành công của Frontend
            return new RedirectView(frontendBaseUrl + "/payment-success?orderId=" + orderId);
        } else {
            // Cập nhật trạng thái đơn hàng sang FAILED/CANCELLED (sẽ gọi OrderService)
            orderService.updateOrderStatus(orderId, "CANCELLED");

            // Chuyển hướng về trang thông báo thất bại của Frontend
            return new RedirectView(frontendBaseUrl + "/payment-failure?orderId=" + orderId);
        }
    }
}