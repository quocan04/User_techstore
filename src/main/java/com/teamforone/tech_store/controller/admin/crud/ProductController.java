package com.teamforone.tech_store.controller.admin.crud;

import com.teamforone.tech_store.dto.request.ProductRequest;
import com.teamforone.tech_store.dto.response.Response;
import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.service.ProductService; // Sử dụng interface đã gộp
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class ProductController {

    private final ProductService productService;

    // Constructor Injection
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public List<Product> getAllProducts() {
        return productService.getAllProducts();
    }

    @PostMapping("/products/add")
    public ResponseEntity<Response> addProduct(@RequestBody ProductRequest productRequest) {
        // Phương thức này hiện đã tồn tại trong ProductService
        Response response = productService.addProduct(productRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/products/update/{id}")
    public ResponseEntity<Response> updateProduct(@PathVariable String id , @RequestBody ProductRequest productRequest) {
        // Phương thức này hiện đã tồn tại trong ProductService
        Response response = productService.updateProduct(id, productRequest);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<Response> deleteProduct(@PathVariable String id) {
        // Phương thức này hiện đã tồn tại trong ProductService
        Response response = productService.deleteProduct(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/products/{id}")
    public Product findProductById(@PathVariable String id){
        // Phương thức này hiện đã tồn tại trong ProductService
        return productService.findProductById(id);
    }

}