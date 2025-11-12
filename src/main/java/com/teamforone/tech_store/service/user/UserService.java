package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.dto.request.AddressRequest;
import com.teamforone.tech_store.dto.request.ChangePasswordRequest;
import com.teamforone.tech_store.dto.request.RegisterRequest;
import com.teamforone.tech_store.dto.request.UpdateProfileRequest;
import com.teamforone.tech_store.model.User;

import java.util.List;

public interface UserService {

    // --- Chức năng Admin (Lấy từ admin.UserService) ---
    List<User> getAllUsers();

    // --- Chức năng User/Auth/Profile/Address (Lấy từ UserServicel) ---
    void register(RegisterRequest request);
    User findByEmail(String email);
    void updateProfile(String email, UpdateProfileRequest request);
    void changePassword(String email, ChangePasswordRequest request);
    void addAddress(String email, AddressRequest request);
    void updateAddress(Long addressId, AddressRequest request);
    void deleteAddress(Long addressId);
}