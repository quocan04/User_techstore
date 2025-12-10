// File: com.teamforone.tech_store.dto.response.PaymentResponse.java
package com.teamforone.tech_store.dto.response;

import lombok.Data;

@Data // Hoặc @Getter, @Setter
public class PaymentResponse {
    private String orderNo;
    private String paymentUrl; // Chứa link VNPAY/VietQR
}