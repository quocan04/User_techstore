package com.teamforone.tech_store.model;


// --- Imports (Rất quan trọng) ---

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items") // Ánh xạ tới bảng 'order_items'
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_item_id", columnDefinition = "CHAR(36)")
    private String orderItemId; // Kiểu String vì là CHAR(36)

    /**
     * Quan hệ n-1 với Orders.
     * Nhiều OrderItem thuộc về 1 Order.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    // Giả định bạn cũng muốn liên kết tới Product (dù SQL không có FK trực tiếp)
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "product_id") // Cần thêm cột product_id vào SQL
    // private Product product;

    // Các ID cho chi tiết sản phẩm (như trong SQL)
    @Column(name = "colorID", columnDefinition = "CHAR(36)")
    private String colorID;

    @Column(name = "sizeID", columnDefinition = "CHAR(36)")
    private String sizeID;

    @Column(name = "storageID", columnDefinition = "CHAR(36)")
    private String storageID;

    @Column(name = "quantity")
    private int quantity;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    /**
     * Trạng thái của mục đơn hàng.
     * Dùng @Enumerated(EnumType.STRING) để lưu tên (PAID, PENDING...)
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
}