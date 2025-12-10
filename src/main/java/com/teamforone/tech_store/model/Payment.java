package com.teamforone.tech_store.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator; // Cần import
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment") // Tên bảng trong database
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator") // Dùng chiến lược UUID
    @Column(name = "payment_id", columnDefinition = "CHAR(36)")
    private String paymentId; // Kiểu String vì là CHAR(36)

    /**
     * Quan hệ 1-1 với Orders.
     * Một Payment chỉ thuộc về một Order.
     */
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "order_id", unique = true, nullable = false)
    private Orders order;

    @Column(name = "amount", precision = 10, scale = 2, nullable = false)
    private BigDecimal amount; // Số tiền thanh toán (nên dùng BigDecimal)

    // --- CÁC TRƯỜNG DÀNH RIÊNG CHO VNPAY ---

    /**
     * Mã tham chiếu giao dịch (vnp_TxnRef)
     * Đây là mã duy nhất bạn tạo ra để gửi cho VNPAY (thường là order_id).
     */
    @Column(name = "vnp_txn_ref", length = 50, unique = true)
    private String vnpTxnRef;

    /**
     * Mã giao dịch của VNPAY (vnp_TransactionNo)
     * VNPAY sẽ trả về mã này khi thanh toán thành công.
     */
    @Column(name = "vnp_transaction_no", length = 50)
    private String vnpTransactionNo;

    /**
     * Mã ngân hàng (vnp_BankCode)
     */
    @Column(name = "bank_code", length = 20)
    private String bankCode;

    /**
     * Trạng thái thanh toán (ví dụ: PENDING, PAID, FAILED)
     */
    @Column(name = "payment_status", length = 20, nullable = false)
    private String paymentStatus;

    /**
     * Thời gian thanh toán thành công (VNPAY trả về)
     */
    @Column(name = "payment_date")
    private LocalDateTime paymentDate;

    /**
     * Nội dung thanh toán (vnp_OrderInfo)
     */
    @Column(name = "transaction_content", length = 255)
    private String transactionContent;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    // --- Lifecycle Callbacks ---
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}