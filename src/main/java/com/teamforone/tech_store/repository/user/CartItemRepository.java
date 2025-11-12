package com.teamforone.tech_store.repository.user;


// Import Entity mà Repository này sẽ quản lý

import com.teamforone.tech_store.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository (kho) để truy cập dữ liệu cho Bảng CartItem.
 * Spring Data JPA sẽ tự động tạo các phương thức CRUD (save, findById, delete, v.v.).
 */
@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    // JpaRepository<CartItem, String>
    // 1. CartItem: Đây là Entity mà Repository này quản lý.
    // 2. String:   Đây là kiểu dữ liệu của Khóa chính (Primary Key).
    //              Vì 'item_id' trong DB của bạn là CHAR(36) -> ta dùng String.

    // --- CÁC HÀM TRUY VẤN TÙY CHỈNH (CUSTOM QUERY METHODS) ---

    /**
     * Tự động tạo một truy vấn DELETE để xóa TẤT CẢ các CartItem
     * thuộc về một Cart (giỏ hàng) cụ thể.
     * * Rất hữu ích cho chức năng "Clear Cart" (Xóa sạch giỏ hàng).
     *
     * @param cartId ID của Giỏ hàng.
     */
    void deleteAllByCart_CartId(String cartId);

    // Bạn cũng có thể thêm các hàm tìm kiếm phức tạp khác ở đây nếu cần,
    // ví dụ:
    // Optional<CartItem> findByCart_CartIdAndProduct_ProductIdAndColorIDAndSizeIDAndStorageID(
    //     String cartId, String productId, String colorId, String sizeId, String storageId
    // );
}