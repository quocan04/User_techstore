package com.teamforone.tech_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(
            name = "uuid2",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Column(name = "order_item_id", columnDefinition = "CHAR(36)")
    private String orderItemId;

    /**
     * Nhiều OrderItem thuộc về 1 Order
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Orders order;

    // ===== SỬA CHUẨN JAVA BEAN =====

    @Column(name = "colorID", columnDefinition = "CHAR(36)")
    private String colorId;

    @Column(name = "sizeID", columnDefinition = "CHAR(36)")
    private String sizeId;

    @Column(name = "storageID", columnDefinition = "CHAR(36)")
    private String storageId;

    // =================================

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @Column(name = "subtotal", precision = 10, scale = 2, nullable = false)
    private BigDecimal subtotal;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    private OrderStatus orderStatus;
}