package com.teamforone.tech_store.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Data
public class UpdateProfileRequest {
    @NotBlank
    private String fullName;

    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$", message = "SĐT không hợp lệ")
    private String phone;
}