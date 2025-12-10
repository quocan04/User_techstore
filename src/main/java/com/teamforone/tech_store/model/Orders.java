package com.teamforone.tech_store.model;

// --- ĐẦY ĐỦ CÁC IMPORT CẦN THIẾT ---

// Import các thư viện JPA (cho @Entity, @Table, @Id, v.v.)

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
// --- KẾT THÚC IMPORT ---


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
     * Nhiều đơn hàng có thể thuộc về 1 User.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Quan hệ n-1 với Shipping.
     * (Giả định một địa chỉ Shipping có thể được dùng cho nhiều Orders)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shipping_id")
    private Shipping shipping;

    /**
     * Quan hệ n-1 với NhanVien.
     * Một nhân viên có thể xử lý nhiều đơn hàng.
     */
//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "nhanvienID")
//    private NhanVien nhanVien;

    /**
     * Phương thức thanh toán.
     * Dùng @Enumerated(EnumType.STRING) để lưu tên (VNPAY, COD...)
     * thay vì số (0, 1...).
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "payment_method")
    private PaymentMethod paymentMethod;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // --- CÁC QUAN HỆ ĐẢO NGƯỢC ---

    /**
     * Quan hệ 1-n với OrderItem.
     * Một đơn hàng có nhiều món hàng.
     */
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<OrderItem> orderItems;

    /**
     * Quan hệ 1-1 với Payment (chúng ta vừa tạo).
     * 'mappedBy = "order"' trỏ tới tên trường 'order' trong Entity Payment.
     */
    @OneToOne(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Payment payment;

    // --- Lifecycle Callbacks ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}