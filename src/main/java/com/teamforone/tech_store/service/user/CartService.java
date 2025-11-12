package com.teamforone.tech_store.service.user;

// Import các DTO mà service này cần làm việc
import com.teamforone.tech_store.dto.request.AddToCartRequest;
import com.teamforone.tech_store.dto.response.CartResponse;

/**
 * Interface định nghĩa các nghiệp vụ liên quan đến Giỏ hàng của người dùng.
 */
public interface CartService {

    /**
     * Thêm một sản phẩm (với các tùy chọn) vào giỏ hàng của người dùng.
     * Nếu sản phẩm với tùy chọn đó đã tồn tại, cập nhật số lượng.
     *
     * @param userId ID (String) của người dùng đang đăng nhập.
     * @param request DTO chứa thông tin sản phẩm (productId, colorId, v.v.) và số lượng.
     */
    void addToCart(String userId, AddToCartRequest request);

    /**
     * Lấy toàn bộ thông tin giỏ hàng của người dùng.
     *
     * @param userId ID (String) của người dùng.
     * @return CartResponse DTO chứa danh sách các món hàng và tổng tiền.
     */
    CartResponse getCartByUserId(String userId);

    /**
     * Cập nhật số lượng của một món hàng cụ thể trong giỏ.
     *
     * @param cartItemId ID (String) của món hàng (CartItem) cần cập nhật.
     * @param quantity Số lượng mới (ví dụ: 3).
     */
    void updateCartItemQuantity(String cartItemId, int quantity);

    /**
     * Xóa một món hàng ra khỏi giỏ.
     *
     * @param cartItemId ID (String) của món hàng (CartItem) cần xóa.
     */
    void removeCartItem(String cartItemId);

    /**
     * Dọn dẹp giỏ hàng của người dùng (thường gọi sau khi thanh toán thành công).
     *
     * @param userId ID (String) của người dùng.
     */
    void clearCart(String userId);
}