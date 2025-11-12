package com.teamforone.tech_store.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AddressRequest {
    @NotBlank private String recipientName;
    @NotBlank private String phone;
    @NotBlank private String street;
    @NotBlank private String ward;
    @NotBlank private String district;
    @NotBlank private String city;
    private boolean isDefault;
}