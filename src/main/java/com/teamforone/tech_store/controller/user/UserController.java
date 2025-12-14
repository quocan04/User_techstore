package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.model.User;
import com.teamforone.tech_store.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;     // ← DÙNG @Controller, không phải RestController
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")                         // ← Đường dẫn chung
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    // Trang hồ sơ cá nhân
    @GetMapping("/profile")
    public String profile(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("user", currentUser);
        return "user/profile";        // → templates/user/profile.html
    }

    // Các trang khác bạn sẽ làm sau (nếu cần)
    @GetMapping("/address")
    public String address() {
        return "user/address";
    }

    @GetMapping("/change-password")
    public String changePassword() {
        return "user/change-password";
    }
}