// File: com.teamforone.tech_store.controller.user.CartController.java

package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.dto.request.AddToCartRequest;
import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.dto.response.PaymentResponse;
import com.teamforone.tech_store.service.user.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    /**
     * API CHÍNH: GET /api/user/cart (Lấy dữ liệu thật từ DB)
     */
    @GetMapping("/cart")
    public ResponseEntity<CartResponse> getCartApi(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).body(null);
        }
        try {
            String userId = userDetails.getUsername();
            CartResponse cartResponse = cartService.getCartByUserId(userId);

            return ResponseEntity.ok(cartResponse);
        } catch (Exception e) {
            System.err.println("Lỗi Server khi tải giỏ hàng: " + e.getMessage());
            return ResponseEntity.status(500).body(null);
        }
    }

    /**
     * API: POST /api/user/checkout/vnpay (Xử lý thanh toán)
     */
    @PostMapping("/checkout/vnpay")
    public ResponseEntity<PaymentResponse> checkoutVnpay(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody CheckoutRequest request) {

        if (userDetails == null) {
            return ResponseEntity.status(401).body(null);
        }
        try {
            String userId = userDetails.getUsername();
            PaymentResponse paymentResponse = cartService.processVnpayCheckout(userId, request);

            return ResponseEntity.ok(paymentResponse);

        } catch (Exception e) {
            System.err.println("Lỗi Server khi xử lý thanh toán: " + e.getMessage());
            return ResponseEntity.badRequest().body(null);
        }
    }

    // API: Thêm sản phẩm vào giỏ hàng (Giữ nguyên)
    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody AddToCartRequest request) {
        // ... (Logic cũ)
        return ResponseEntity.ok("Đã thêm vào giỏ hàng");
    }

    private String getUserIdFromUserDetails(UserDetails userDetails) {
        return userDetails.getUsername();
    }
}