package com.teamforone.tech_store.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class VnPayPaymentResponse {
    private String status;
    private String message;
    private String paymentUrl;
}