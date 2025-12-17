package com.teamforone.tech_store.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "users")
public class User {

    // Khắc phục: Dùng UUID (String) cho user_id
    @Id
    @UuidGenerator
    @Column(name = "user_id", columnDefinition = "CHAR(36)")
    private String id;

    // Tên đăng nhập
    @Column(name = "username", nullable = false, unique = true)
    private String username;

    // Mật khẩu (ánh xạ tới password_hash)
    @Column(name = "password_hash", nullable = false)
    private String password;

    // Tên đầy đủ (Khắc phục lỗi: Ánh xạ tường minh tới full_name)
    @Column(name = "full_name", nullable = false)
    private String fullName;

    // Email
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    // Số điện thoại
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;


    // Trạng thái (Enum)
    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('ACTIVE','LOCKED') DEFAULT 'ACTIVE'")
    private Status status = Status.ACTIVE;

    // Mối quan hệ với Address
    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Address> addresses = new ArrayList<>();

    // Thời gian tạo
    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;


    @UpdateTimestamp
    @Column(name = "updated_at")
    private Date updatedAt;

    public boolean isEnabled() {
        return this.status == Status.ACTIVE;
    }

    // Định nghĩa Enum Status
    public enum Status {
        ACTIVE,
        LOCKED;



        private static Status toEnum(String status) {
            for (Status item : values()) {
                if (item.toString().equalsIgnoreCase(status)) return item;
            }
            return null;
        }
    }
}