package com.teamforone.tech_store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartResponse {

    // ID của giỏ hàng
    private String cartId;

    // Danh sách các món hàng
    private List<CartItemResponse> items;

    // Tổng số lượng các món hàng (ví dụ: 2 cái A, 1 cái B -> tổng là 3)
    private int totalItems;

    // Tổng tiền tạm tính (tổng của các 'subtotal' trong list items)
    private BigDecimal temporaryTotal;

    // Tổng tiền cuối cùng (sau khi áp dụng giảm giá, phí vận chuyển...)
    // Tạm thời có thể bằng temporaryTotal
    private BigDecimal grandTotal;
}