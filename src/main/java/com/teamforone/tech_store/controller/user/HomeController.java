package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor // Sử dụng Lombok để tự động inject ProductService
public class HomeController {

    // Khai báo final để sử dụng Constructor Injection (khuyến nghị hơn @Autowired field)
    private final ProductService productService;

    // Chức năng 1: Ánh xạ tới "/" (Tên trả về là "home" từ HomeControllerl)
    @GetMapping({"/", "/home"})
    public String home(Model model) {
        // Gọi phương thức getAllProducts từ ProductService đã gộp
        model.addAttribute("products", productService.getAllProducts());
        return "home";
    }
}