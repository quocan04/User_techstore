package com.teamforone.tech_store.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "coupons")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Coupon {

    @Id
    @UuidGenerator
    @Column(name = "coupon_id", columnDefinition = "CHAR(36)")
    private String couponId;

    @Column(name = "code", nullable = false, unique = true, length = 20)
    private String code;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", columnDefinition = "ENUM('percent','fixed','free_shipping')")
    private CouponType type;

    @Column(name = "value", precision = 10, scale = 2)
    private BigDecimal value;

    @Column(name = "min_order_amount", precision = 10, scale = 2)
    private BigDecimal minOrderAmount;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "starts_at")
    private Date startsAt;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "ends_at")
    private Date endsAt;

    @Column(name = "usage_limit")
    private Integer usageLimit;

    @Column(name = "used_count", columnDefinition = "INT DEFAULT 0")
    private Integer usedCount = 0;

    @Column(name = "active", columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean active = true;

    public enum CouponType {
        PERCENT,
        FIXED,
        FREE_SHIPPING;

        private static CouponType toEnum(String value) {
            for (CouponType type : CouponType.values()) {
                if (type.toString().equalsIgnoreCase(value)) return type;
            }
            return null;
        }
    }
}