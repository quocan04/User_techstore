package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.repository.admin.crud.UserProductRepository;
import com.teamforone.tech_store.model.Product;
import java.util.List;

public interface ProductService {
    List<Product> getAllProducts();
    Product getProductById(String id);

    List<Product> getProductsByCategorySlug(String slug);
}