package com.teamforone.tech_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.annotations.UuidGenerator;

import java.util.Date;
import java.util.UUID;

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
    private String cartItemID;

    @JoinColumn(name = "cart_id", nullable = false, columnDefinition = "CHAR(36)")
    private String cart;

    @JoinColumn(name = "product_id", nullable = false, columnDefinition = "CHAR(36)")
    private String product;

    @JoinColumn(name = "colorID", nullable = false, columnDefinition = "CHAR(36)")
    private String color;

    @JoinColumn(name = "sizeID", nullable = false, columnDefinition = "CHAR(36)")
    private String displaySize;

    @JoinColumn(name = "storageID", nullable = false, columnDefinition = "CHAR(36)")
    private String storage;

    @Column(name = "quantity", nullable = false, columnDefinition = "INT DEFAULT 1")
    private int quantity;

    @CreationTimestamp
    @Column(name = "added_at", nullable = false)
    private Date addedAt;
}
