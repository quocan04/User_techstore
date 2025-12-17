package com.teamforone.tech_store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItemResponse {

    // ID của mục giỏ hàng (để xóa hoặc cập nhật)
    private String cartItemId;

    // ID của sản phẩm (để click vào xem chi tiết)
    private String productId;

    // Tên sản phẩm
    private String productName;

    // Ảnh
    private String defaultImage;

    // Số lượng
    private int quantity;

    // Giá của 1 sản phẩm (nên dùng BigDecimal cho tiền tệ)
    private BigDecimal unitPrice;

    // Tổng tiền của mục này (quantity * unitPrice)
    private BigDecimal subtotal;

    // Các thông tin chi tiết (lấy từ Color, Storage, DisplaySize)
    private String colorName;
    private String storageName; // Ví dụ: "12GB/256GB"
    private String sizeName;    // Ví dụ: "6.7 inch"
}