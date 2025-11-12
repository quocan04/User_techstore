package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.dto.request.RegisterRequest;
import com.teamforone.tech_store.service.user.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService UserService;

    @GetMapping("/login")
    public String login() {
        return "auth/login";
    }

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("request", new RegisterRequest());
        return "auth/register";
    }

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute("request") RegisterRequest request,
                           BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "auth/register";
        }
        try {
            UserService.register(request);
            return "redirect:/auth/login?success";
        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }


    }
}