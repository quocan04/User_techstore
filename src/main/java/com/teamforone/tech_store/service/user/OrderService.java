package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.model.Orders;

import java.util.List;

/**
 * Interface định nghĩa các nghiệp vụ liên quan đến Đơn hàng.
 */
public interface OrderService {

    /**
     * Tạo đơn hàng từ giỏ hàng của người dùng.
     */
    Orders createOrderFromCart(String userId, CheckoutRequest request);

    /**
     * Cập nhật trạng thái thanh toán của đơn hàng (PAID, CANCELLED).
     */
    void updateOrderStatus(String orderId, String newStatus);

    /**
     * Lấy danh sách đơn hàng của người dùng.
     */
    List<Orders> getOrdersByUserId(String userId);

    /**
     * Lấy chi tiết một đơn hàng.
     */
    Orders getOrderDetails(String orderId);
}