package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.model.Categories;
import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.repository.admin.crud.CategoryRepository;
import com.teamforone.tech_store.repository.admin.crud.UserProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service("userProductService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private UserProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public List<Product> getProductsByCategorySlug(String slug) {
        return productRepository.findProductsByCategorySlug(slug);
    }
}
