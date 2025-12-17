package com.teamforone.tech_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Table(name = "orders") // Tên bảng trong DB
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Orders {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_id", columnDefinition = "CHAR(36)")
    private String orderId; // Kiểu String vì là CHAR(36)

    @Column(name = "order_no", unique = true, length = 20)
    private String orderNo; // Mã đơn hàng (ví dụ: "ORD-2025-001")

    /**
     * Quan hệ n-1 với User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Quan hệ n-1 với Shipping.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;

    /**
     * Phương thức thanh toán.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    /**
     * THÊM MỚI: Trạng thái đơn hàng (PENDING, PAID, CANCELLED...)
     * Giúp fix lỗi biên dịch setStatus trong OrderServiceImpl
     */
    @Column(name = "status", length = 50)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // --- CÁC QUAN HỆ ĐẢO NGƯỢC ---

    /**
     * Quan hệ 1-n với OrderItem.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;

    /**
     * Quan hệ 1-1 với Payment.
     */
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    // --- Lifecycle Callbacks ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}