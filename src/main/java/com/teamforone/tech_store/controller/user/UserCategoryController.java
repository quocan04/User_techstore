package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.service.user.UserProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/category")
public class UserCategoryController {

    private static final Logger log =
            LoggerFactory.getLogger(UserCategoryController.class);

    @Autowired
    private UserProductService userProductService;

    @GetMapping("/{slug}")
    public String viewCategory(@PathVariable String slug, Model model) {

        log.debug("🔍 Truy cập category với slug = {}", slug);

        List<Product> products = userProductService.getProductsByCategorySlug(slug);

        if (products.isEmpty()) {
            log.warn("⚠ Category slug={} tồn tại nhưng không có sản phẩm hoặc không tồn tại", slug);
        }

        model.addAttribute("products", products);
        model.addAttribute("categorySlug", slug);
        model.addAttribute("pageTitle", "Danh mục: " + slug);

        return "user/category";
    }
}
