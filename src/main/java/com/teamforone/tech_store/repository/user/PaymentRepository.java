package com.teamforone.tech_store.repository.user;

// Import Entity mà Repository này quản lý

import com.teamforone.tech_store.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository (kho) để truy cập dữ liệu cho Bảng Payment.
 * Spring Data JPA sẽ tự động tạo các phương thức CRUD.
 */
@Repository
public interface PaymentRepository extends JpaRepository<Payment, String> {

    // JpaRepository<Payment, String>
    // 1. Payment: Đây là Entity mà Repository này quản lý.
    // 2. String:    Đây là kiểu dữ liệu của Khóa chính (Primary Key) 'payment_id'.

    // --- CÁC HÀM TRUY VẤN TÙY CHỈNH (CUSTOM QUERY METHODS) ---

    /**
     * Tự động tạo một truy vấn SELECT để tìm Giao dịch (Payment)
     * dựa trên Mã tham chiếu giao dịch (vnp_TxnRef).
     *
     * * Đây là hàm CỰC KỲ QUAN TRỌNG. Khi VNPAY gọi về (IPN/Return),
     * * chúng ta sẽ dùng 'vnp_TxnRef' (chính là 'order_id' lúc gửi đi)
     * * để tìm lại bản ghi Payment và cập nhật trạng thái của nó.
     *
     * Tương đương: SELECT * FROM payment WHERE vnp_txn_ref = ?
     *
     * @param vnpTxnRef Mã tham chiếu giao dịch (thường là order_id).
     * @return Một Optional chứa Payment nếu tìm thấy.
     */
    Optional<Payment> findByVnpTxnRef(String vnpTxnRef);

    /**
     * Tự động tạo một truy vấn SELECT để tìm Giao dịch (Payment)
     * dựa trên Order ID.
     *
     * @param orderId ID của đơn hàng.
     * @return Một Optional chứa Payment nếu tìm thấy.
     */
    Optional<Payment> findByOrder_OrderId(String orderId);
}