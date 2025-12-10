package com.teamforone.tech_store.model;

/**
 * Đại diện cho các phương thức thanh toán được hỗ trợ.
 * * Các giá trị trong Enum này (COD, VNPAY, MOMO, STRIPE)
 * phải khớp chính xác 100% (phân biệt hoa thường)
 * với các giá trị trong ENUM của database:
 * * CREATE TABLE orders (
 * ...
 * payment_method ENUM('COD','VNPAY','MOMO','STRIPE'),
 * ...
 * );
 * * Khi dùng với JPA, chúng ta sẽ dùng @Enumerated(EnumType.STRING)
 * để JPA lưu trữ tên của Enum (ví dụ: "VNPAY")
 * thay vì thứ tự của nó (ví dụ: 1).
 */
public enum PaymentMethod {
    COD,
    VNPAY,
    MOMO,
    STRIPE
}