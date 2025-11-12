package com.teamforone.tech_store.service.impl;
// Đặt trong package implementation chung

import com.teamforone.tech_store.dto.request.ProductRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.repository.admin.crud.ProductRepository;
import com.teamforone.tech_store.service.ProductService; // Interface đã gộp

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;

    public ProductServiceImpl(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    // --- CRUD/Tìm kiếm (Các phương thức này cần phải tồn tại trong Interface) ---

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Response addProduct(ProductRequest product) {
        // ... (Logic thêm sản phẩm) ...
        String status = product.getStatus();

        Product newProduct = Product.builder()
                // ...
                .productStatus(Product.Status.toEnum(status))
                .build();

        productRepository.save(newProduct);
        return Response.builder().status(HttpStatus.OK.value()).message("Product added successfully").build();
    }

    @Override
    public Response updateProduct(String id, ProductRequest product) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct == null) {
            return Response.builder().status(HttpStatus.NOT_FOUND.value()).message("Product not found").build();
        }
        // ... (Logic cập nhật) ...
        existingProduct.setProductStatus(Product.Status.toEnum(product.getStatus()));

        productRepository.save(existingProduct);
        return Response.builder().status(HttpStatus.OK.value()).message("Product updated successfully").build();
    }

    @Override
    public Response deleteProduct(String id) {
        Product existingProduct = productRepository.findById(id).orElse(null);
        if (existingProduct == null) {
            return Response.builder().status(HttpStatus.NOT_FOUND.value()).message("Product not found").build();
        }

        productRepository.delete(existingProduct);
        return Response.builder().status(HttpStatus.OK.value()).message("Product deleted successfully").build();
    }

    // Phương thức này có thể bị lỗi nếu Interface không có nó.
    // Nếu bạn muốn giữ lại tên này, hãy đảm bảo Interface ProductService cũng có findProductById.
    // Nếu Interface chỉ có getProductById, hãy XÓA @Override này.
    // Giả sử Interface CÓ cả hai (hoặc bạn sẽ xóa nó khỏi Interface).
    public Product findProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    // --- KHẮC PHỤC LỖI "method does not override or implement..." ---
    // Phương thức bị thiếu từ Interface ProductService (cần phải implement)
    @Override
    public Product getProductById(String id) {
        // Sử dụng logic tìm kiếm đã tồn tại
        return productRepository.findById(id).orElse(null);
    }
}