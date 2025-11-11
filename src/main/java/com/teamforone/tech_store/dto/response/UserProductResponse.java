package com.teamforone.tech_store.dto.response;

import com.teamforone.tech_store.model.Product;
import lombok.Data;

@Data
public class UserProductResponse {
    private String id;
    private String name;
    private double price;

    public static UserProductResponse fromEntity(Product product) {
        UserProductResponse dto = new UserProductResponse();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setPrice(product.getPrice());
        return dto;
    }
}
