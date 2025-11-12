package com.teamforone.tech_store.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AddToCartRequest {

    // Dùng @NotBlank để yêu cầu các ID không được rỗng
    // Các ID này tương ứng với các cột CHAR(36) trong bảng cart_items

    @NotBlank(message = "Product ID không được để trống")
    private String productId;

    @NotBlank(message = "Color ID không được để trống")
    private String colorID;

    @NotBlank(message = "Size ID không được để trống")
    private String sizeID;

    @NotBlank(message = "Storage ID không được để trống")
    private String storageID;

    // Dùng @Min(1) để đảm bảo số lượng thêm vào ít nhất là 1
    @Min(value = 1, message = "Số lượng phải ít nhất là 1")
    private int quantity;
}