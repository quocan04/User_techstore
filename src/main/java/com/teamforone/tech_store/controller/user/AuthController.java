package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.dto.request.RegisterRequest;
import com.teamforone.tech_store.model.User;
import com.teamforone.tech_store.service.user.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @GetMapping("/dangnhap")
    public String showLoginForm() {
        return "user/auth/login";
    }

    @PostMapping("/dangnhap")
    public String processLogin(
            @RequestParam("username") String email,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes
    ) {
        try {
            User user = userService.login(email, password);

            // ✅ LƯU ID DƯỚI DẠNG STRING (vì ID đã là String UUID)
            session.setAttribute("userId", user.getId());
            session.setAttribute("userEmail", user.getEmail());
            session.setAttribute("userName", user.getFullName());
            session.setAttribute("isLoggedIn", true);

            System.out.println("✅ Đã lưu session:");
            System.out.println("   - userId: " + user.getId());
            System.out.println("   - userName: " + user.getFullName());
            System.out.println("   - isLoggedIn: true");

            redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
            return "redirect:/home";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Email hoặc mật khẩu không chính xác!");
            return "redirect:/auth/dangnhap";
        }
    }

    @GetMapping({"/register", "/dangky"})
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "user/auth/register";
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute RegisterRequest request,
            BindingResult result,
            Model model) {

        if (result.hasErrors()) {
            return "user/auth/register";
        }

        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Xác nhận mật khẩu không khớp!");
            return "user/auth/register";
        }

        try {
            userService.register(request);
            model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/auth/dangnhap";
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/auth/register";
        }
    }

    @GetMapping("/dangxuat")
    public String logout(HttpSession session, RedirectAttributes redirectAttributes) {
        session.invalidate();
        redirectAttributes.addFlashAttribute("success", "Đã đăng xuất thành công!");
        return "redirect:/home";
    }

    @GetMapping("/logout")
    public String logoutRedirect() {
        return "redirect:/auth/dangxuat";
    }

    @GetMapping("/profile")
    public String profile(HttpSession session, Model model, RedirectAttributes redirect) {
        // ✅ LẤY TRỰC TIẾP DƯỚI DẠNG STRING (vì ID là String UUID)
        String userId = (String) session.getAttribute("userId");

        if (userId == null || userId.isEmpty()) {
            redirect.addFlashAttribute("error", "Vui lòng đăng nhập để xem hồ sơ!");
            return "redirect:/auth/dangnhap";
        }

        try {
            User user = userService.findById(userId);
            model.addAttribute("user", user);
            return "user/profile";

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không tìm thấy thông tin người dùng!");
            session.invalidate();
            return "redirect:/auth/dangnhap";
        }
    }
}