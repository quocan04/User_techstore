package com.teamforone.tech_store.service.user.impl;

import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.model.*;
import com.teamforone.tech_store.repository.admin.crud.user.OrderRepository;
import com.teamforone.tech_store.repository.admin.crud.user.PaymentRepository;
import com.teamforone.tech_store.repository.admin.crud.user.ShippingRepository;
import com.teamforone.tech_store.service.user.CartService;
import com.teamforone.tech_store.service.user.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teamforone.tech_store.repository.admin.UserRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Implementation của OrderService.
 * @Service đánh dấu đây là Spring Bean để có thể autowire.
 */
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CartService cartService;
    private final ShippingRepository shippingRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            PaymentRepository paymentRepository,
                            UserRepository userRepository,
                            CartService cartService,
                            ShippingRepository shippingRepository) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.cartService = cartService;
        this.shippingRepository = shippingRepository;
    }

    @Override
    @Transactional
    public Orders createOrderFromCart(String userId, CheckoutRequest request) {
        // 1. Lấy giỏ hàng
        CartResponse cart = cartService.getCartByUserId(userId);
        if (cart == null || cart.getItems() == null || cart.getItems().isEmpty()) {
            throw new RuntimeException("Giỏ hàng của bạn đang trống.");
        }

        // 2. Lấy User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại."));

        // 3. Tạo và Lưu Shipping
        Shipping shippingInfo = new Shipping();
        // TODO: Set các field từ request
        // shippingInfo.setRecipientName(request.getRecipientName());
        // shippingInfo.setPhone(request.getPhone());
        // shippingInfo.setAddress(request.getAddress());
        // shippingInfo.setNotes(request.getNotes());
        Shipping savedShipping = shippingRepository.save(shippingInfo);

        // 4. Tạo Orders
        Orders newOrder = new Orders();
        newOrder.setUser(user);
        newOrder.setShipping(savedShipping);
        newOrder.setPaymentMethod(PaymentMethod.valueOf(request.getPaymentMethod()));
        newOrder.setTotalAmount(cart.getGrandTotal());
        newOrder.setOrderNo("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        newOrder.setCreatedAt(LocalDateTime.now());

        // 5. Lưu Orders
        Orders savedOrder = orderRepository.save(newOrder);

        // 6. Tạo Payment với trạng thái PENDING
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(savedOrder.getTotalAmount());
        payment.setPaymentStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        // 7. Xóa giỏ hàng
        cartService.clearCart(userId);

        // 8. Trả về đơn hàng
        return savedOrder;
    }

    @Override
    @Transactional
    public void updateOrderStatus(String orderId, String newStatus) {
        Payment payment = paymentRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giao dịch cho đơn hàng: " + orderId));

        payment.setPaymentStatus(newStatus);

        if ("PAID".equals(newStatus)) {
            payment.setPaymentDate(LocalDateTime.now());
        }

        paymentRepository.save(payment);
    }

    @Override
    public List<Orders> getOrdersByUserId(String userId) {
        return orderRepository.findByUser_Id(userId);
    }

    @Override
    public Orders getOrderDetails(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy đơn hàng."));
    }
}