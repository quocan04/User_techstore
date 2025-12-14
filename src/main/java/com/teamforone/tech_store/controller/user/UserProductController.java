package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@RequiredArgsConstructor // Tốt hơn @Autowired, không cần viết constructor
public class UserProductController {

    // Không cần @Autowired nữa khi dùng RequiredArgsConstructor + final
    private final ProductService productService;

    /**
     * Trang chi tiết sản phẩm
     * URL: /product/123 hoặc /product/iphone-16-pro-max
     */
    @GetMapping("/product/{id}")
    public String productDetail(@PathVariable("id") String id, Model model) {

        // Lấy sản phẩm theo ID (String hoặc Long đều được tùy bạn thiết kế)
        Product product = productService.getProductById(id);

        // Nếu không tìm thấy → chuyển sang trang 404 đẹp (tùy chọn)
        if (product == null) {
            return "error/404"; // bạn có thể tạo file templates/error/404.html
        }

        model.addAttribute("product", product);
        return "user/product-detail"; // → templates/user/product-detail.html
    }
}