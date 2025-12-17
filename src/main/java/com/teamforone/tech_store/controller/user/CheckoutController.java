package com.teamforone.tech_store.controller.user;

import jakarta.servlet.http.HttpSession;
import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.dto.response.Response2;
import com.teamforone.tech_store.dto.response.VnPayPaymentResponse;
import com.teamforone.tech_store.model.Orders;
import com.teamforone.tech_store.service.user.OrderService;
import com.teamforone.tech_store.service.user.VnPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.math.BigDecimal;
import java.util.Map;

@Controller
@RequestMapping("/user")
public class CheckoutController {

    private final OrderService orderService;
    private final VnPayService vnPayService;

    @Autowired
    public CheckoutController(OrderService orderService, VnPayService vnPayService) {
        this.orderService = orderService;
        this.vnPayService = vnPayService;
    }

    // Trang giao diện thanh toán
    @GetMapping("/Payment")
    public String homePayment() {
        return "user/Payment";
    }

    /**
     * POST /user/vnpay
     * Bước 1: Lấy userId từ Session, tạo đơn hàng và lấy link thanh toán VNPAY.
     */
    @PostMapping("/vnpay")
    public ResponseEntity<?> createOrderAndVnPayPayment(
            HttpSession session,
            @RequestBody CheckoutRequest request) {
        try {
            // 1. Lấy userId thực tế từ Session mà mày đã lưu khi Login
            String userId = (String) session.getAttribute("userId");
            System.out.println("🔥 VNPAY Checkout - userId từ Session: " + userId);

            if (userId == null || userId.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(new Response2<>("error", "Vui lòng đăng nhập để tiếp tục thanh toán", null));
            }

            // 2. Tạo đơn hàng từ giỏ hàng (OrderService sẽ tính tổng tiền từ CartItem của userId này)
            Orders newOrder = orderService.createOrderFromCart(userId, request);
            System.out.println("🔥 Đơn hàng đã tạo: " + newOrder.getOrderId() + " - Tổng tiền: " + newOrder.getTotalAmount());

            // 3. Khởi tạo số tiền cho VNPAY (Số tiền * 100 theo quy định VNPAY)
            long amountVnPay = newOrder.getTotalAmount().multiply(new BigDecimal(100)).longValue();

            // 4. Tạo URL thanh toán
            String paymentUrl = vnPayService.createVnPayPaymentUrl(
                    newOrder.getOrderId(),
                    amountVnPay,
                    request.getPaymentMethod()
            );

            // 5. Trả về link cho Frontend
            VnPayPaymentResponse response = new VnPayPaymentResponse("success", "Khởi tạo thanh toán VNPAY thành công", paymentUrl);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            System.err.println("❌ Lỗi thanh toán: " + e.getMessage());
            e.printStackTrace();
            Response2<?> response = new Response2<>("error", "Lỗi xử lý đơn hàng: " + e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /user/vnpay-return
     * Bước 2: Nhận phản hồi từ VNPAY sau khi user thực hiện thao tác trên web ngân hàng.
     */
    @GetMapping("/vnpay-return")
    public RedirectView processVnPayReturn(@RequestParam Map<String, String> vnp_Params) {

        System.out.println("🔥 VNPAY Return Params: " + vnp_Params);

        // Kiểm tra chữ ký và trạng thái giao dịch từ VNPAY
        boolean isSuccess = vnPayService.processVnPayPayment(vnp_Params);
        String orderId = vnp_Params.get("vnp_TxnRef");

        // Đường dẫn quay lại trang chủ hoặc trang lịch sử đơn hàng của mày
        String redirectUrl = "/user/home";

        if (isSuccess) {
            // Cập nhật trạng thái đơn hàng trong DB thành Đã thanh toán (PAID)
            orderService.updateOrderStatus(orderId, "PAID");
            System.out.println("✅ Thanh toán thành công cho đơn: " + orderId);
            return new RedirectView(redirectUrl + "?payment=success&orderId=" + orderId);
        } else {
            // Cập nhật trạng thái đơn hàng thành Đã hủy hoặc Thất bại
            orderService.updateOrderStatus(orderId, "CANCELLED");
            System.err.println("❌ Thanh toán thất bại hoặc bị hủy cho đơn: " + orderId);
            return new RedirectView(redirectUrl + "?payment=fail&orderId=" + orderId);
        }
    }
}