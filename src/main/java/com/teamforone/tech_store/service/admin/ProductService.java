package com.teamforone.tech_store.service.admin; // Chuyển sang package chung

import com.teamforone.tech_store.dto.request.ProductRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Product;

import java.util.List;

// Interface đã gộp chứa cả chức năng User và Admin CRUD
public interface ProductService {

    // Phương thức chung (User và Admin): Lấy tất cả sản phẩm
    List<Product> getAllProducts();

    // Phương thức Admin: Thêm sản phẩm
    Response addProduct(ProductRequest product);

    // Phương thức Admin: Cập nhật sản phẩm
    Response updateProduct(String id, ProductRequest product);

    // Phương thức Admin: Xóa sản phẩm
    Response deleteProduct(String id);

    // Phương thức chung (User và Admin): Tìm kiếm theo ID
    Product findProductById(String id);

    // Phương thức bị thiếu gây lỗi trong ProductServiceImpl trước đó
    // Cần thêm vào để đồng bộ hóa với lớp Implementation
    Product getProductById(String id);
}