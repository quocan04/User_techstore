package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.service.user.ProductService;
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

    @Autowired
    private ProductService productService;


    @GetMapping("/{slug}")
    public String viewCategory(@PathVariable("slug") String slug, Model model) {
        List<Product> products = productService.getProductsByCategorySlug(slug);
        model.addAttribute("products", products);
        model.addAttribute("categorySlug", slug);
        return "user/category";
    }
}
