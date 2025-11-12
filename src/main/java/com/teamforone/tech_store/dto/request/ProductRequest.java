package com.teamforone.tech_store.dto.request;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class ProductRequest {
    private String name;
    private String slug;
    private String description;
    private String brandId;
    private String categoryId;
    private String imageUrl;
    private String status;
}
