package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.model.CTProducts;
import com.teamforone.tech_store.model.PhoneSpecs;
import com.teamforone.tech_store.service.user.ProductService;
import com.teamforone.tech_store.service.user.CTProductService;
import com.teamforone.tech_store.service.user.PhoneSpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import java.util.ArrayList;

import java.util.List;

@Controller
@RequestMapping("/product")
public class UserProductController {

    @Autowired
    @Qualifier("userProductService")
    private ProductService productService;

    @Autowired
    private PhoneSpecsService phoneSpecsService;

    @Autowired
    private CTProductService ctProductService;

    // Hiển thị chi tiết sản phẩm
    @GetMapping("/{slug}")
    public String viewProductDetail(@PathVariable("slug") String slug, Model model) {
        System.out.println("═══════════════════════════════════════════");
        System.out.println("🔍 [PRODUCT DETAIL] slug = " + slug);

        Product product = productService.getProductBySlug(slug);

        if (product == null) {
            System.out.println("❌ Không tìm thấy product với slug = " + slug);
            return "error/404";
        }

        String productId = product.getId();
        System.out.println("✅ Product ID: " + productId);

        // Lấy specs
        PhoneSpecs specs = phoneSpecsService.getSpecsByProductId(productId);

        // Lấy variants
        List<CTProducts> variants = ctProductService.getVariantsByProductId(productId);
        CTProducts selectedVariant = null;

        if (!variants.isEmpty()) {
            selectedVariant = ctProductService.getCheapestVariant(productId);
            System.out.println("💰 Giá: " + selectedVariant.getPrice());
        }

        model.addAttribute("product", product);
        model.addAttribute("specs", specs);
        model.addAttribute("variants", variants);
        model.addAttribute("selectedVariant", selectedVariant);
        model.addAttribute("comments", new ArrayList<>()); // Thêm sau

        System.out.println("═══════════════════════════════════════════");
        return "user/product-detail";
    }

    // Xử lý thêm comment - SỬA DÙNG SLUG
    @PostMapping("/{slug}/comment")
    public String addComment(
            @PathVariable("slug") String slug,
            @RequestParam("content") String content) {

        System.out.println("💬 [COMMENT] slug = " + slug);
        System.out.println("💬 Content: " + content);

        // TODO: Lưu comment vào database
        // Product product = productService.getProductBySlug(slug);
        // commentService.save(new Comment(product.getId(), content));

        return "redirect:/product/" + slug;
    }
}