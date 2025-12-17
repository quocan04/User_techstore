package com.teamforone.tech_store.controller.user;

import jakarta.servlet.http.HttpSession;
import com.teamforone.tech_store.dto.request.AddToCartRequest;
import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.dto.response.PaymentResponse;
import com.teamforone.tech_store.service.user.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping("/cart")
    public ResponseEntity<?> getCartApi(HttpSession session) {
        System.out.println("🔥 GET /cart - Session ID: " + session.getId());
        System.out.println("🔥 GET /cart - userId: " + session.getAttribute("userId"));

        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Vui lòng đăng nhập"));
        }

        try {
            CartResponse cartResponse = cartService.getCartByUserId(userId);

            System.out.println("🔥 GET /cart - Response: " + cartResponse);
            System.out.println("🔥 GET /cart - Items count: " +
                    (cartResponse.getItems() != null ? cartResponse.getItems().size() : 0));

            return ResponseEntity.ok(cartResponse);
        } catch (Exception e) {
            System.err.println("Lỗi Server khi tải giỏ hàng: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.status(500)
                    .body(Map.of("error", "Lỗi server: " + e.getMessage()));
        }
    }

    // ======================================================
    // UPDATE QUANTITY (HÀM MỚI THÊM VÀO ĐỂ FIX 404)
    // ======================================================
    @PostMapping("/cart/update/{cartItemId}")
    public ResponseEntity<?> updateQuantity(
            HttpSession session,
            @PathVariable String cartItemId,
            @RequestParam int quantity) {

        System.out.println("🔥 POST /cart/update - cartItemId: " + cartItemId);
        System.out.println("🔥 POST /cart/update - new quantity: " + quantity);

        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Vui lòng đăng nhập"));
        }

        try {
            cartService.updateCartItemQuantity(cartItemId, quantity);

            // Lấy lại giỏ hàng mới nhất để UI cập nhật tiền
            CartResponse updatedCart = cartService.getCartByUserId(userId);
            return ResponseEntity.ok(updatedCart);
        } catch (Exception e) {
            System.err.println("Lỗi khi cập nhật số lượng: " + e.getMessage());
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/checkout/vnpay")
    public ResponseEntity<?> checkoutVnpay(
            HttpSession session,
            @RequestBody CheckoutRequest request) {

        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Vui lòng đăng nhập"));
        }

        try {
            PaymentResponse paymentResponse = cartService.processVnpayCheckout(userId, request);
            return ResponseEntity.ok(paymentResponse);
        } catch (Exception e) {
            System.err.println("Lỗi Server khi xử lý thanh toán: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cart/add")
    public ResponseEntity<?> addToCart(
            HttpSession session,
            @RequestBody AddToCartRequest request) {

        System.out.println("🔥 POST /cart/add - Session ID: " + session.getId());
        System.out.println("🔥 POST /cart/add - isLoggedIn: " + session.getAttribute("isLoggedIn"));
        System.out.println("🔥 POST /cart/add - userId: " + session.getAttribute("userId"));
        System.out.println("🔥 POST /cart/add - Request: " + request);

        String userId = (String) session.getAttribute("userId");

        if (userId == null || userId.isEmpty()) {
            return ResponseEntity.status(401)
                    .body(Map.of("error", "Vui lòng đăng nhập để thêm vào giỏ hàng"));
        }

        try {
            cartService.addToCart(userId, request);
            return ResponseEntity.ok(Map.of("message", "Đã thêm vào giỏ hàng"));
        } catch (Exception e) {
            System.err.println("Lỗi khi thêm vào giỏ hàng: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/cart/remove")
    public ResponseEntity<?> removeItem(HttpSession session, @RequestBody Map<String, String> body) {
        String cartItemId = body.get("cartItemId");
        String userId = (String) session.getAttribute("userId");

        System.out.println("🔥 POST /cart/remove - cartItemId: " + cartItemId);

        cartService.removeCartItem(cartItemId);

        // Sau khi xóa, lấy lại giỏ hàng mới nhất trả về cho client
        CartResponse updatedCart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(updatedCart);
    }
}