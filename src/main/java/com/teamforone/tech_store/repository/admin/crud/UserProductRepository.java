package com.teamforone.tech_store.repository.admin.crud;

import com.teamforone.tech_store.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserProductRepository extends JpaRepository<Product, String> {

    // Method đã có
    List<Product> findByCategoryIdIn(List<String> categoryIds);
    List<Product> findByNameContainingIgnoreCase(String keyword);

    // Thêm method này
    Product findBySlug(String slug);
}