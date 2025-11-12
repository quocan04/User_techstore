package com.teamforone.tech_store.service.user;
// Đặt trong package service chung hơn để phục vụ cả Admin và User

import com.teamforone.tech_store.dto.request.AddressRequest;
import com.teamforone.tech_store.dto.request.ChangePasswordRequest;
import com.teamforone.tech_store.dto.request.RegisterRequest; // Sửa import DTO
import com.teamforone.tech_store.dto.request.UpdateProfileRequest; // Sửa import DTO
import com.teamforone.tech_store.model.Address; // Sửa Entity Address
import com.teamforone.tech_store.model.User; // Sửa Entity User
import com.teamforone.tech_store.repository.admin.AddressRepository; // Sửa Repository Address
import com.teamforone.tech_store.repository.admin.UserRepository; // Sửa Repository User
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    // Tên lớp chuẩn: UserServiceImpl

    // Khai báo Dependency (Sửa lỗi đánh máy Repository)
    private final UserRepository userRepository;
    private final AddressRepository addressRepository;
    private final PasswordEncoder passwordEncoder;

    // --- PHƯƠNG THỨC ADMIN ---

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // --- PHƯƠNG THỨC USER/AUTH ---

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email đã tồn tại!");
        }
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            throw new RuntimeException("Mật khẩu xác nhận không khớp!");
        }

        // Đã sử dụng Entity User đã gộp và sửa lỗi
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy người dùng"));
    }

    @Override
    @Transactional
    public void updateProfile(String email, UpdateProfileRequest request) {
        User user = findByEmail(email);
        user.setFullName(request.getFullName());
        user.setPhone(request.getPhone());
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void changePassword(String email, ChangePasswordRequest request) {
        User user = findByEmail(email);
        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Mật khẩu cũ không đúng!");
        }
        if (!request.getNewPassword().equals(request.getConfirmNewPassword())) {
            throw new RuntimeException("Mật khẩu mới không khớp!");
        }
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }

    @Override
    @Transactional
    public void addAddress(String email, AddressRequest request) {
        User user = findByEmail(email);
        if (request.isDefault() && !user.getAddresses().isEmpty()) {
            user.getAddresses().forEach(a -> a.setDefault(false));
        }
        Address address = new Address();
        address.setUser(user);
        address.setRecipientName(request.getRecipientName());
        address.setPhone(request.getPhone());
        address.setStreet(request.getStreet());
        address.setWard(request.getWard());
        address.setDistrict(request.getDistrict());
        address.setCity(request.getCity());
        address.setDefault(request.isDefault());
        addressRepository.save(address);
    }

    @Override
    @Transactional
    public void updateAddress(Long addressId, AddressRequest request) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ!"));

        address.setRecipientName(request.getRecipientName());
        address.setPhone(request.getPhone());
        address.setStreet(request.getStreet());
        address.setWard(request.getWard());
        address.setDistrict(request.getDistrict());
        address.setCity(request.getCity());

        if (request.isDefault() && !address.isDefault()) {
            address.getUser().getAddresses().forEach(a -> a.setDefault(false));
        }
        address.setDefault(request.isDefault());

        addressRepository.save(address);
    }

    @Override
    @Transactional
    public void deleteAddress(Long addressId) {
        Address address = addressRepository.findById(addressId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy địa chỉ cần xóa!"));

        addressRepository.delete(address);
    }
}