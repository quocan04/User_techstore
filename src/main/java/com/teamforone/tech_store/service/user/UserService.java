package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.dto.request.AddressRequest;
import com.teamforone.tech_store.dto.request.ChangePasswordRequest;
import com.teamforone.tech_store.dto.request.RegisterRequest;
import com.teamforone.tech_store.dto.request.UpdateProfileRequest;
import com.teamforone.tech_store.model.User;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface UserService {

    List<User> getAllUsers();

    void register(RegisterRequest request);
    User findByEmail(String email);
    User getCurrentUser();
    User findById(String  id);

    void updateProfile(String email, UpdateProfileRequest request);
    void changePassword(String email, ChangePasswordRequest request);
    void addAddress(String email, AddressRequest request);
    void updateAddress(Long addressId, AddressRequest request);
    void deleteAddress(Long addressId);

    User login(String email, String password);
    User loginAndAuthenticate(String email, String password);

}