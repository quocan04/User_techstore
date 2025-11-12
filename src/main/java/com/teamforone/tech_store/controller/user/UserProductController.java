package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.model.PhoneSpecs;
import com.teamforone.tech_store.service.user.ProductService;
import com.teamforone.tech_store.service.user.PhoneSpecsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/product")
public class UserProductController {

    @Autowired
    private ProductService productService;

    @Autowired
    private PhoneSpecsService phoneSpecsService;

    @GetMapping("/{id}")
    public String viewProductDetail(@PathVariable("id") String productId, Model model) {

        // Lấy sản phẩm
        Product product = productService.getProductById(productId);

        // Lấy thông số kỹ thuật
        PhoneSpecs specs = phoneSpecsService.getSpecsByProductId(productId);

        model.addAttribute("product", product);
        model.addAttribute("specs", specs);

        return "user/product-detail";
    }
}
