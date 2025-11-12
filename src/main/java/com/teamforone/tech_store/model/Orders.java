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

import java.math.BigDecimal;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Orders {
    @Id
    @UuidGenerator
    @Column(name = "order_id", columnDefinition = "CHAR(36)")
    private String orderID;

    @Column(name = "order_no", unique = true, length = 20)
    private String orderNo;

    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "CHAR(36)")
    private String user;

    @Column(name = "total_amount", precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @JoinColumn(name = "shipping_id", nullable = false, columnDefinition = "CHAR(36)")
    private String shipping;

    @Column(name = "payment_method", columnDefinition = "ENUM('cod','vnpay','momo','stripe')")
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @CreationTimestamp
    @Column(name = "created_at")
    private Date createdAt;

    @JoinColumn(name = "nhanvienID", nullable = false, columnDefinition = "CHAR(36)")
    private String nhanvien;

    public enum PaymentMethod {
        MOMO,
        VNPAY,
        STRIPE,
        COD;

        private static PaymentMethod toEnum(String value) {
            for(PaymentMethod method : PaymentMethod.values()){
                if (method.toString().equalsIgnoreCase(value)) return method;
            }
            return null;
        }
    }
}
