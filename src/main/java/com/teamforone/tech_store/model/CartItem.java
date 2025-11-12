package com.teamforone.tech_store.model;


import com.teamforone.tech_store.model.Color;
import com.teamforone.tech_store.model.DisplaySize;
import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.model.Storage;
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
@Table(name = "cart_items")
public class CartItem {
    @Id
    @UuidGenerator
    @Column(name = "item_id", columnDefinition = "CHAR(36)")
    private String cartItemId;  // ← Đổi từ cartItemID → cartItemId

    // ✅ Liên kết với Cart
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id", nullable = false, columnDefinition = "CHAR(36)")
    private Cart cart;  // ← Đổi từ String → Cart object

    // ✅ Liên kết với Product (giả định bạn có Product model)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, columnDefinition = "CHAR(36)")
    private Product product;  // ← Đổi từ String → Product object

    // ✅ Liên kết với Color (giả định bạn có Color model)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "colorID", nullable = false, columnDefinition = "CHAR(36)")
    private Color color;  // ← Đổi từ String → Color object

    // ✅ Liên kết với DisplaySize
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sizeID", nullable = false, columnDefinition = "CHAR(36)")
    private DisplaySize displaySize;  // ← Đổi từ String → DisplaySize object

    // ✅ Liên kết với Storage
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "storageID", nullable = false, columnDefinition = "CHAR(36)")
    private Storage storage;  // ← Đổi từ String → Storage object

    @Column(name = "quantity", nullable = false, columnDefinition = "INT DEFAULT 1")
    private int quantity;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false)
    private Date addedAt;
}