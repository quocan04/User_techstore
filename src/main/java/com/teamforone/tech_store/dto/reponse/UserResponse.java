package com.teamforone.tech_store.dto.response;

import com.teamforone.tech_store.model.User; // <--- Cần thêm import này cho Entity User
import com.teamforone.tech_store.dto.response.AddressResponse;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {
    private String id;
    private String email;
    private String fullName;
    private String phone;

    private List<AddressResponse> addresses;

    // Constructor từ Entity (nếu cần map)
    public UserResponse(User user) {
        this.id = user.getId();
        this.email = user.getEmail();
        this.fullName = user.getFullName();
        this.phone = user.getPhone();
        this.addresses = user.getAddresses().stream()
                .map(AddressResponse::new)
                .toList();
    }
}