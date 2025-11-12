package com.teamforone.tech_store.service.admin;

import com.teamforone.tech_store.dto.request.CategoryRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Categories;

import java.util.List;

public interface CategoryService {
    Response deleteCategory(String id);
    List<Categories> getAllCategories();
    Response addCategory(CategoryRequest category);
    Response updateCategory(String id, CategoryRequest category);
    Categories findCategoryById(String id);
}
