package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.dto.request.AddToCartRequest;
import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.dto.response.Response2;
import com.teamforone.tech_store.service.user.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    // --- Helper để lấy ID User hiện tại ---
    private String getCurrentUserId() {
        // **QUAN TRỌNG:** Bạn cần thay thế logic này
        // bằng cách lấy ID (String) của User đang đăng nhập từ Spring Security Context.

        // Ví dụ tạm thời:
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // if (authentication == null || !authentication.isAuthenticated()) {
        //     throw new RuntimeException("Bạn cần đăng nhập để thực hiện chức năng này.");
        // }
        // return ((UserDetails) authentication.getPrincipal()).getUsername(); // Giả định username là ID

        return "user_test_id"; // Thay bằng logic lấy ID User thực tế (String)
    }

    /**
     * POST /api/user/cart
     * Thêm sản phẩm vào giỏ hàng (hoặc cập nhật số lượng nếu đã tồn tại)
     */
    @PostMapping
    public ResponseEntity<Response2> addToCart(@RequestBody AddToCartRequest request) {
        try {
            String userId = getCurrentUserId();
            cartService.addToCart(userId, request);

            Response2 response = new Response2("success", "Sản phẩm đã được thêm vào giỏ hàng!", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Response2 response = new Response2("error", e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * GET /api/user/cart
     * Xem chi tiết giỏ hàng của user
     */
    @GetMapping
    public String getCart() {
        String userId = getCurrentUserId();
        CartResponse cartResponse = cartService.getCartByUserId(userId);

        return "user/cart";
    }

    /**
     * PUT /api/user/cart/{cartItemId}
     * Cập nhật số lượng của một mục trong giỏ hàng
     */
    @PutMapping("/{cartItemId}")
    public ResponseEntity<Response2> updateCartItemQuantity(@PathVariable String cartItemId,
                                                           @RequestParam int quantity) {
        try {
            // Logic kiểm tra quyền sở hữu giỏ hàng sẽ nằm trong CartService
            cartService.updateCartItemQuantity(cartItemId, quantity);

            Response2 response = new Response2("success", "Cập nhật số lượng thành công!", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Response2 response = new Response2("error", e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    /**
     * DELETE /api/user/cart/{cartItemId}
     * Xóa một mục khỏi giỏ hàng
     */
    @DeleteMapping("/{cartItemId}")
    public ResponseEntity<Response2> removeCartItem(@PathVariable String cartItemId) {
        try {
            // Logic kiểm tra quyền sở hữu giỏ hàng sẽ nằm trong CartService
            cartService.removeCartItem(cartItemId);

            Response2 response = new Response2("success", "Xóa sản phẩm khỏi giỏ hàng thành công!", null);
            return ResponseEntity.ok(response);
        } catch (RuntimeException e) {
            Response2 response = new Response2("error", e.getMessage(), null);
            return new ResponseEntity<>(response, HttpStatus.NOT_FOUND);
        }
    }
}