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
    private String cartId;
    private Integer totalItems;
    private BigDecimal temporaryTotal;
    private BigDecimal grandTotal;
    private List<CartItemResponse> items;
}