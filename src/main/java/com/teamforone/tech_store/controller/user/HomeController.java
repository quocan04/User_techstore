package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.service.user.UserProductService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import com.teamforone.tech_store.model.Product;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.List;

@Controller
public class HomeController {

    @Autowired
    private UserProductService userProductService;

    @GetMapping("/home")
    public String home(Model model, HttpSession session) {
        model.addAttribute("products", userProductService.getAllProducts());

        // Lấy thông tin đăng nhập từ session
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String userName = (String) session.getAttribute("userName");

        model.addAttribute("isLoggedIn", isLoggedIn != null && isLoggedIn);
        model.addAttribute("userName", userName);

        // Debug log
        System.out.println("🔍 Home - isLoggedIn: " + isLoggedIn);
        System.out.println("🔍 Home - userName: " + userName);

        return "user/home";
    }

    @GetMapping("/")
    public String index(Model model, HttpSession session) {
        model.addAttribute("products", userProductService.getAllProducts());

        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String userName = (String) session.getAttribute("userName");

        model.addAttribute("isLoggedIn", isLoggedIn != null && isLoggedIn);
        model.addAttribute("userName", userName);

        return "user/home";
    }

    @GetMapping("/search")
    public String searchProducts(
            @RequestParam("keyword") String keyword,
            Model model,
            HttpSession session) {

        List<Product> products = userProductService.searchProducts(keyword);
        model.addAttribute("products", products);
        model.addAttribute("keyword", keyword);

        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String userName = (String) session.getAttribute("userName");

        model.addAttribute("isLoggedIn", isLoggedIn != null && isLoggedIn);
        model.addAttribute("userName", userName);

        return "user/home";
    }
}