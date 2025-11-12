package com.teamforone.tech_store.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CategoryRequest {
    private String categoryName;
    private String parentCategory;
}
