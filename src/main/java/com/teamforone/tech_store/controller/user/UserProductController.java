package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.model.PhoneSpecs;
import com.teamforone.tech_store.service.user.ProductService;
import com.teamforone.tech_store.service.user.PhoneSpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class UserProductController {

    @Autowired
    @Qualifier("userProductService")
    private ProductService productService;

    @Autowired
    private PhoneSpecsService phoneSpecsService;

    // Sử dụng SLUG thay vì ID cho URL thân thiện hơn
    @GetMapping("/{slug}")
    public String viewProductDetail(@PathVariable("slug") String slug, Model model) {
        System.out.println("DEBUG: Truy cập chi tiết sản phẩm với slug = " + slug);

        // Lấy product theo slug
        Product product = productService.getProductBySlug(slug);

        if (product == null) {
            System.out.println("DEBUG: Không tìm thấy sản phẩm với slug = " + slug);
            return "redirect:/404"; // Hoặc return "error/404";
        }

        System.out.println("DEBUG: Tìm thấy sản phẩm: " + product.getName());

        // Lấy specs theo product ID
        PhoneSpecs specs = phoneSpecsService.getSpecsByProductId(product.getId());

        model.addAttribute("product", product);
        model.addAttribute("specs", specs);
        // model.addAttribute("comments", new ArrayList<>()); // Thêm sau khi có comments

        return "user/product-detail";
    }

    // Giữ lại endpoint cũ nếu muốn hỗ trợ truy cập bằng ID
    @GetMapping("/id/{id}")
    public String viewProductDetailById(@PathVariable("id") String productId, Model model) {
        Product product = productService.getProductById(productId);

        if (product == null) {
            return "redirect:/404";
        }

        PhoneSpecs specs = phoneSpecsService.getSpecsByProductId(productId);

        model.addAttribute("product", product);
        model.addAttribute("specs", specs);

        return "user/product-detail";
    }
}