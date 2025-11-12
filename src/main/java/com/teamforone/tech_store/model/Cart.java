package com.teamforone.tech_store.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cart")
public class Cart {
    @Id
    @UuidGenerator
    @Column(name = "cart_id", columnDefinition = "CHAR(36)")
    private String cartId;  // ← Đổi tên từ cartID → cartId (convention)

    // ✅ QUAN TRỌNG: Dùng @ManyToOne để liên kết với User
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private User user;  // ← Đổi từ String → User object

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;
}