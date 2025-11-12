package com.teamforone.tech_store.service;

import com.teamforone.tech_store.dto.request.ProductRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Product;

import java.util.List;

public interface ProductService {

    // Phương thức chung (User/Admin)
    List<Product> getAllProducts();
    Product getProductById(String id);

    // Phương thức Admin CRUD (Bổ sung để Controller không lỗi)
    Response addProduct(ProductRequest product);
    Response updateProduct(String id, ProductRequest product);
    Response deleteProduct(String id);

    // Phương thức Tìm kiếm (Bổ sung để Controller không lỗi)
    Product findProductById(String id);
}