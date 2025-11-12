package com.teamforone.tech_store.dto.request;

import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class RegisterRequest {
    @NotBlank @Email
    private String email;

    @NotBlank
    @Size(min = 8)
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!]).*$",
            message = "Mật khẩu cần: 8+ ký tự, 1 số, 1 chữ hoa, 1 ký tự đặc biệt")
    private String password;

    @NotBlank
    private String confirmPassword;

    @NotBlank
    private String fullName;

    @NotBlank
    @Pattern(regexp = "^\\d{10,11}$", message = "SĐT không hợp lệ")
    private String phone;
}