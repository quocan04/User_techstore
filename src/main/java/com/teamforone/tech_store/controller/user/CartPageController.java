package com.teamforone.tech_store.controller.user;
import jakarta.servlet.http.HttpSession;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class CartPageController {
    @GetMapping("/cart")
    public String showCartPage(HttpSession session, Model model) {
        Boolean isLoggedIn = (Boolean) session.getAttribute("isLoggedIn");
        String userName = (String) session.getAttribute("userName");

        model.addAttribute("isLoggedIn", isLoggedIn != null ? isLoggedIn : false);
        model.addAttribute("userName", userName != null ? userName : "");

        return "user/cart";
    }
}
