package com.teamforone.tech_store.model;

/**
 * Đại diện cho các trạng thái của một OrderItem (mục đơn hàng).
 * * Các giá trị trong Enum này (PENDING, PAID, PROCESSING, v.v.)
 * phải khớp chính xác 100% (phân biệt hoa thường)
 * với các giá trị trong ENUM của database:
 * * CREATE TABLE order_items (
 * ...
 * order_status ENUM('PENDING','PAID','PROCESSING','SHIPPED','DELIVERED','CANCELLED','RETURNED')
 * ...
 * );
 * * Khi dùng với JPA, chúng ta sẽ dùng @Enumerated(EnumType.STRING)
 * để JPA lưu trữ tên của Enum (ví dụ: "PAID")
 * thay vì thứ tự của nó (ví dụ: 1).
 */
public enum OrderStatus {
    PENDING,
    PAID,
    PROCESSING,
    SHIPPED,
    DELIVERED,
    CANCELLED,
    RETURNED
}
