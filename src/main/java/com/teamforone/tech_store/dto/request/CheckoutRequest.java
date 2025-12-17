package com.teamforone.tech_store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.math.BigDecimal; // Import thêm cái này

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CheckoutRequest {
    private String shippingId;
    private String paymentMethod;
    private String note;
    private BigDecimal distance; // ⬅️ PHẢI CÓ DÒNG NÀY để hết lỗi getDistance()
}