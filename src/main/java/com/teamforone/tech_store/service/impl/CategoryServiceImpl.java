package com.teamforone.tech_store.service.impl;

import com.teamforone.tech_store.dto.request.CategoryRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Categories;
import com.teamforone.tech_store.repository.admin.crud.CategoryRepository;
import com.teamforone.tech_store.service.admin.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryServiceImpl implements CategoryService {
    @Autowired
    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Response deleteCategory(String id) {
        Categories existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory == null) {
            return Response.builder()
                    .status(HttpStatus.NOT_FOUND.value())
                    .message("Category not found")
                    .build();
        }
        categoryRepository.delete(existingCategory);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Category deleted successfully")
                .build();
    }

    @Override
    public List<Categories> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    public Response addCategory(CategoryRequest category) {
        String name = category.getCategoryName();
        Categories newCategory = Categories.builder()
                .categoryName(name)
                .parentCategory(null)
                .build();
        categoryRepository.save(newCategory);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Category added successfully")
                .build();
    }

    @Override
    public Response updateCategory(String id, CategoryRequest request) {
        String name = request.getCategoryName();
        // Lấy category cần update
        Categories existingCategory = categoryRepository.findById(id).orElse(null);
        if (existingCategory == null) return null;

        // Cập nhật tên
        existingCategory.setCategoryName(name);

        // Nếu người dùng nhập parentCategory (có ID cha)
        if (request.getParentCategory() != null && !request.getParentCategory().isEmpty()) {
            Categories parent = categoryRepository
                    .findById(request.getParentCategory())
                    .orElse(null);
            existingCategory.setParentCategory(parent);
        } else {
            // Nếu không nhập gì thì gán null (xoá quan hệ cha)
            existingCategory.setParentCategory(null);
        }

        categoryRepository.save(existingCategory);
        return Response.builder()
                .status(HttpStatus.OK.value())
                .message("Category updated successfully")
                .build();
    }

    @Override
    public Categories findCategoryById(String id) {
        return categoryRepository.findById(id).orElse(null);
    }
}
