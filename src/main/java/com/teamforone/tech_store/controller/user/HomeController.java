package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
public class HomeController {


    private final ProductService productService;


    @GetMapping({"/", "/home"})
    public String home(Model model) {

        model.addAttribute("products", productService.getAllProducts());
        return "user/home";
    }
}