package com.teamforone.tech_store.service.user.impl;

import com.teamforone.tech_store.dto.request.AddToCartRequest;
import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.dto.response.CartItemResponse;
import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.dto.response.PaymentResponse;
import com.teamforone.tech_store.model.Cart;
import com.teamforone.tech_store.model.CartItem;
import com.teamforone.tech_store.model.User;
import com.teamforone.tech_store.repository.admin.crud.CartItemRepository;
import com.teamforone.tech_store.repository.admin.crud.CartRepository;
import com.teamforone.tech_store.repository.admin.crud.user.UserRepository;
import com.teamforone.tech_store.service.user.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           UserRepository userRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
    }

    // =================================================================
    // PHƯƠNG THỨC: processVnpayCheckout
    // =================================================================
    @Override
    @Transactional
    public PaymentResponse processVnpayCheckout(String userId, CheckoutRequest request) {

        // Lấy CartResponse thật (Tự động gọi getCartByUserId)
        CartResponse cartData = getCartByUserId(userId);

        if (cartData.getTotalItems() == 0) {
            throw new RuntimeException("Giỏ hàng trống, không thể thanh toán.");
        }

        // 2. Tạo Order No
        String orderNo = "ORD-" + System.currentTimeMillis();

        // 3. Tạo PaymentResponse DTO và link QR VietQR (Dùng thông tin MB BANK)
        String bankCode = "970422";
        String accountNumber = "0364424536";
        String accountName = "TRAN TAN HAO";
        BigDecimal amount = cartData.getGrandTotal() != null ? cartData.getGrandTotal() : BigDecimal.ZERO;

        String qrLink = "https://img.vietqr.io/image/" + bankCode + "-" + accountNumber + "-compact2.png?amount=" + amount.intValue() + "&addInfo=TTDH" + orderNo + "&accountName=" + accountName;

        PaymentResponse response = new PaymentResponse();
        response.setOrderNo(orderNo);
        response.setPaymentUrl(qrLink);

        return response;
    }
    // =================================================================


    @Override
    @Transactional
    public void addToCart(String userId, AddToCartRequest request) {
        // Logic cũ
    }

    @Override
    public CartResponse getCartByUserId(String userId) {
        // 1. Tìm Cart của user
        Optional<Cart> cartOpt = cartRepository.findByUser_Id(userId);

        CartResponse response = new CartResponse();

        if (cartOpt.isEmpty()) {
            // Giỏ hàng trống
            response.setCartId(null);
            response.setItems(new ArrayList<>());
            response.setTotalItems(0);
            response.setTemporaryTotal(BigDecimal.ZERO);
            response.setGrandTotal(BigDecimal.ZERO);
            return response;
        }

        Cart cart = cartOpt.get();

        // 2. KHÔI PHỤC LOGIC TRUY VẤN DB THẬT
        // Dòng này yêu cầu phương thức findCartItemsWithDetailsNative(String cartID) phải có trong CartItemRepository.
        List<Object[]> rawResults = cartItemRepository.findCartItemsWithDetailsNative(cart.getCartID());

        // 3. Convert sang CartItemResponse
        List<CartItemResponse> items = rawResults.stream()
                .map(this::convertToCartItemResponse)
                .collect(Collectors.toList());

        // 4. Tính toán
        int totalItems = items.stream().mapToInt(CartItemResponse::getQuantity).sum();
        BigDecimal temporaryTotal = items.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // 5. Set response
        response.setCartId(cart.getCartID());
        response.setItems(items);
        response.setTotalItems(totalItems);
        response.setTemporaryTotal(temporaryTotal);
        response.setGrandTotal(temporaryTotal);

        return response;
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(String cartItemId, int quantity) {
        // Logic cũ
    }

    @Override
    @Transactional
    public void removeCartItem(String cartItemId) {
        // Logic cũ
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        // Logic cũ
    }

    // Helper method: Convert Object[] từ native query sang CartItemResponse
    private CartItemResponse convertToCartItemResponse(Object[] row) {
        // Chú ý: Cần đảm bảo thứ tự các trường trong truy vấn SQL Native khớp với các cast này
        CartItemResponse response = new CartItemResponse();
        response.setCartItemId((String) row[0]);
        response.setProductId((String) row[1]);
        response.setProductName((String) row[2]);
        response.setDefaultImage((String) row[3]);
        response.setQuantity(((Number) row[4]).intValue());
        response.setUnitPrice((BigDecimal) row[5]);
        response.setSubtotal((BigDecimal) row[6]);
        response.setColorName((String) row[7]);
        response.setStorageName((String) row[8]);
        response.setSizeName((String) row[9]);
        return response;
    }
}