package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.repository.admin.crud.ProductRepository;
import com.teamforone.tech_store.service.user.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service("userProductService")
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }
}