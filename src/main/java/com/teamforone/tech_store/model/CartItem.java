package com.teamforone.tech_store.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UuidGenerator;
import org.hibernate.type.SqlTypes;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "cart_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @UuidGenerator
    @Column(name = "item_id", nullable = false, columnDefinition = "BINARY(16)")
    private UUID itemId; // ⬅️ BỎ @JdbcTypeCode

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    // ⬇️ GIỮ NGUYÊN - VẪN DÙNG @JdbcTypeCode
    @Column(name = "product_id", nullable = false, columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID productId;

    @Column(name = "colorid", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID colorId;

    @Column(name = "sizeid", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID sizeId;

    @Column(name = "storageid", columnDefinition = "CHAR(36)")
    @JdbcTypeCode(SqlTypes.CHAR)
    private UUID storageId;

    @Column(name = "quantity", nullable = false)
    private int quantity;

    @CreationTimestamp
    @Column(name = "added_at", updatable = false)
    private Date addedAt;
}