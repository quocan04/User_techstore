package com.teamforone.tech_store.service.user;

// Import các DTO mà service này cần làm việc
import com.teamforone.tech_store.dto.request.AddToCartRequest;
import com.teamforone.tech_store.dto.request.CheckoutRequest; // IMPORT MỚI
import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.dto.response.PaymentResponse; // IMPORT MỚI

/**
 * Interface định nghĩa các nghiệp vụ liên quan đến Giỏ hàng của người dùng.
 */
public interface CartService {

    /**
     * Thêm một sản phẩm (với các tùy chọn) vào giỏ hàng của người dùng.
     */
    void addToCart(String userId, AddToCartRequest request);

    /**
     * Lấy toàn bộ thông tin giỏ hàng của người dùng.
     */
    CartResponse getCartByUserId(String userId);

    /**
     * PHƯƠNG THỨC MỚI (ĐÃ BỊ THIẾU): Xử lý thanh toán VNPAY/VietQR.
     * Khai báo phương thức để giải quyết lỗi biên dịch trong Controller.
     */
    PaymentResponse processVnpayCheckout(String userId, CheckoutRequest request);

    /**
     * Cập nhật số lượng của một món hàng cụ thể trong giỏ.
     */
    void updateCartItemQuantity(String cartItemId, int quantity);

    /**
     * Xóa một món hàng ra khỏi giỏ.
     */
    void removeCartItem(String cartItemId);

    /**
     * Dọn dẹp giỏ hàng của người dùng (thường gọi sau khi thanh toán thành công).
     */
    void clearCart(String userId);
}