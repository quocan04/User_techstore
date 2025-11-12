package com.teamforone.tech_store.controller.admin.crud;

import com.teamforone.tech_store.dto.request.CategoryRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Categories;
import com.teamforone.tech_store.service.admin.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class CategoryController {
    @Autowired
    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping("/categories")
    public List<Categories> getAllCategories() {
        return categoryService.getAllCategories();
    }

    @PostMapping("/categories/add")
    public ResponseEntity<Response> addCategory(@RequestBody CategoryRequest category) {
        Response response = categoryService.addCategory(category);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/categories/update/{id}")
    public ResponseEntity<Response> updateCategory(@PathVariable String id, @RequestBody CategoryRequest category) {
        Response response = categoryService.updateCategory(id, category);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/categories/delete/{id}")
    public ResponseEntity<Response> deleteCategory(@PathVariable String id) {
        Response response = categoryService.deleteCategory(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/categories/{id}")
    public Categories findCategoryById(@PathVariable String id) {
        return categoryService.findCategoryById(id);
    }
}
