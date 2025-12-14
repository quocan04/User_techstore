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

import java.security.Principal;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    // =======================
    // HIỂN THỊ FORM ĐĂNG NHẬP
    // =======================
    @GetMapping("/dangnhap")
    public String showLoginForm() {
        return "user/auth/login";    // => src/main/resources/templates/auth/login.html
    }

    // ============================
    // XỬ LÝ ĐĂNG NHẬP NGƯỜI DÙNG
    // ============================
    @PostMapping("/dangnhap")
    public String processLogin(
            @RequestParam("username") String email,
            @RequestParam("password") String password,
            HttpSession session,
            RedirectAttributes redirectAttributes   // Dùng RedirectAttributes thay vì Model khi redirect
    ) {
        try {
            User user = userService.login(email, password);


            session.setAttribute("userId", user.getId());
            

//            // Optional: lưu thêm tên để hiển thị ở header
//            session.setAttribute("userName", user.getFullName());

            redirectAttributes.addFlashAttribute("success", "Đăng nhập thành công!");
            return "redirect:/";   // hoặc redirect:/user/profile nếu muốn vào thẳng hồ sơ

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Email hoặc mật khẩu không chính xác!");
            return "redirect:/dangnhap";   // quay lại trang đăng nhập
        }
    }

    // Thêm vào AuthController (cùng class với login)

    @GetMapping({"/register", "/dangky"})
    public String showRegisterForm(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "user/auth/register"; // → templates/auth/register.html
    }

    @PostMapping("/register")
    public String processRegister(
            @Valid @ModelAttribute RegisterRequest request,
            BindingResult result,
            Model model) {

        // Nếu validate lỗi (email sai, mật khẩu yếu, v.v.)
        if (result.hasErrors()) {
            return "user/auth/register";
        }

        // Kiểm tra xác nhận mật khẩu
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            model.addAttribute("errorMessage", "Xác nhận mật khẩu không khớp!");
            return "user/auth/register";
        }

        try {
            userService.register(request);
            model.addAttribute("successMessage", "Đăng ký thành công! Vui lòng đăng nhập.");
            return "redirect:/auth/dangnhap"; // hoặc /auth/login
        } catch (IllegalArgumentException e) {
            model.addAttribute("errorMessage", e.getMessage());
            return "user/auth/register";
        }
    }

    // ============================
    // ĐĂNG XUẤT
    // ============================
    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate(); // xóa session người dùng
        return "redirect:/auth/dangnhap";
    }
    @GetMapping("/login")
    public String redirectToDangNhap() {
        return "redirect:/auth/dangnhap";
    }

    @GetMapping("/user/profile")
    public String profile(HttpSession session, Model model, RedirectAttributes redirect) {

        // ĐÚNG CÁCH: Ép kiểu về Long (vì đã lưu kiểu Long)
        Long userId = (Long) session.getAttribute("userId");

        if (userId == null) {
            redirect.addFlashAttribute("error", "Vui lòng đăng nhập để xem hồ sơ!");
            return "redirect:/dangnhap";
        }

        try {
            User user = userService.findById(userId);
            model.addAttribute("user", user);
            return "user/profile";

        } catch (Exception e) {
            redirect.addFlashAttribute("error", "Không tìm thấy thông tin người dùng!");
            session.removeAttribute("userId");
            return "redirect:/dangnhap";
        }
    }
}
