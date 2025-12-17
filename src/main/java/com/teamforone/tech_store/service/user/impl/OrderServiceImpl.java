package com.teamforone.tech_store.service.user.impl;

import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.model.*;
import com.teamforone.tech_store.repository.admin.UserRepository;
import com.teamforone.tech_store.repository.admin.crud.CartItemRepository;
import com.teamforone.tech_store.repository.admin.crud.CartRepository;
import com.teamforone.tech_store.repository.admin.crud.CTProductRepository;
import com.teamforone.tech_store.repository.admin.crud.user.OrderRepository;
import com.teamforone.tech_store.repository.admin.crud.user.PaymentRepository;
import com.teamforone.tech_store.repository.admin.crud.user.ShippingRepository;
import com.teamforone.tech_store.service.user.CartService;
import com.teamforone.tech_store.service.user.OrderService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final CTProductRepository ctProductRepository;
    private final ShippingRepository shippingRepository;
    private final CartService cartService;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository,
                            PaymentRepository paymentRepository,
                            UserRepository userRepository,
                            CartRepository cartRepository,
                            CartItemRepository cartItemRepository,
                            CTProductRepository ctProductRepository,
                            ShippingRepository shippingRepository,
                            CartService cartService) {
        this.orderRepository = orderRepository;
        this.paymentRepository = paymentRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.ctProductRepository = ctProductRepository;
        this.shippingRepository = shippingRepository;
        this.cartService = cartService;
    }

    @Override
    @Transactional
    public Orders createOrderFromCart(String userId, CheckoutRequest request) {
        // 1. Lấy thông tin User
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại."));

        // 2. Lấy Cart & Kiểm tra CartItems
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseThrow(() -> new RuntimeException("Giỏ hàng không tồn tại."));

        List<CartItem> cartItems = cartItemRepository.findByCart_CartId(cart.getCartId());
        if (cartItems.isEmpty()) {
            throw new RuntimeException("Giỏ hàng trống, không thể tạo đơn hàng.");
        }

        // 3. Tính toán tổng tiền từ giá các biến thể sản phẩm
        BigDecimal totalAmount = BigDecimal.ZERO;
        for (CartItem item : cartItems) {
            String pidStr = item.getProductId().toString();
            CTProducts variant = ctProductRepository.findFirstByProductId(pidStr)
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy giá cho SP ID: " + pidStr));

            BigDecimal price = BigDecimal.valueOf(variant.getPrice());
            BigDecimal itemTotal = price.multiply(new BigDecimal(item.getQuantity()));
            totalAmount = totalAmount.add(itemTotal);
        }

        log.info("🔥 Tổng tiền đơn hàng: {}", totalAmount);

        // 4. Khởi tạo và Lưu thông tin Shipping (Đã fix lỗi user_id null)
        Shipping shipping = new Shipping();
        shipping.setUser(user); // Gán User để tránh lỗi user_id cannot be null

        // Gán khoảng cách từ request (nếu FE gửi lên)
        if (request.getDistance() != null) {
            shipping.setDistance(request.getDistance());
        } else {
            shipping.setDistance(BigDecimal.ZERO); // Giá trị mặc định nếu không có
        }
        shipping.setPricePerKm(new BigDecimal("5000")); // Giá cước mặc định

        // Lưu shipping vào DB trước khi gán vào Order
        Shipping savedShipping = shippingRepository.save(shipping);

        // 5. Tạo và lưu Đơn hàng (Orders)
        Orders newOrder = new Orders();
        newOrder.setUser(user);
        newOrder.setShipping(savedShipping);
        newOrder.setTotalAmount(totalAmount);
        newOrder.setOrderNo("ORD-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase());
        newOrder.setCreatedAt(LocalDateTime.now());
        newOrder.setPaymentMethod(PaymentMethod.VNPAY);
        newOrder.setStatus("PENDING"); // Trạng thái đơn hàng chờ xử lý

        Orders savedOrder = orderRepository.save(newOrder);

        // 6. Tạo thông tin Giao dịch (Payment)
        Payment payment = new Payment();
        payment.setOrder(savedOrder);
        payment.setAmount(totalAmount);
        payment.setPaymentStatus("PENDING");
        payment.setCreatedAt(LocalDateTime.now());
        paymentRepository.save(payment);

        return savedOrder;
    }

    @Override
    @Transactional
    public void updateOrderStatus(String orderId, String newStatus) {
        Payment payment = paymentRepository.findByOrder_OrderId(orderId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy thông tin giao dịch."));

        // Cập nhật trạng thái thanh toán
        payment.setPaymentStatus(newStatus);

        if ("PAID".equals(newStatus)) {
            payment.setPaymentDate(LocalDateTime.now());

            // Tìm đơn hàng để lấy userId và xóa giỏ hàng
            Orders order = orderRepository.findById(orderId).orElse(null);
            if (order != null && order.getUser() != null) {
                cartService.clearCart(order.getUser().getId());
                log.info("✅ Đã xóa giỏ hàng cho User: {}", order.getUser().getId());
            }
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
                .orElseThrow(() -> new RuntimeException("Đơn hàng không tồn tại."));
    }
}