package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.model.User;
// Đảm bảo import UserService đã được gộp (giả định là com.teamforone.tech_store.service.UserService)
import com.teamforone.tech_store.service.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/admin/users")
@RequiredArgsConstructor // Thay thế Constructor Injection thủ công
public class UserController {

    // Đã sửa lỗi Injection (chỉ dùng final/RequiredArgsConstructor)
    private final UserService userService;

    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }
}