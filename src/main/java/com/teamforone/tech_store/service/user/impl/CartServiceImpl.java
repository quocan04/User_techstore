package com.teamforone.tech_store.service.user.impl;

import com.teamforone.tech_store.dto.request.AddToCartRequest;
import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.model.Cart;
import com.teamforone.tech_store.model.CartItem;
import com.teamforone.tech_store.model.User;
import com.teamforone.tech_store.repository.user.CartItemRepository;
import com.teamforone.tech_store.repository.user.CartRepository;
import com.teamforone.tech_store.repository.user.UserRepository;
import com.teamforone.tech_store.service.user.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;

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

    @Override
    @Transactional
    public void addToCart(String userId, AddToCartRequest request) {
        // 1. Tìm hoặc tạo Cart cho user
        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new RuntimeException("Người dùng không tồn tại"));
                    Cart newCart = new Cart();
                    newCart.setUser(user);  // ✅ Bây giờ có thể set User object
                    return cartRepository.save(newCart);
                });

        // 2. TODO: Kiểm tra CartItem đã tồn tại chưa và thêm/cập nhật
        throw new RuntimeException("Chức năng đang phát triển");
    }

    @Override
    public CartResponse getCartByUserId(String userId) {
        Cart cart = cartRepository.findByUser_UserId(userId).orElse(null);

        CartResponse response = new CartResponse();

        if (cart == null) {
            response.setItems(new ArrayList<>());
            response.setGrandTotal(BigDecimal.ZERO);
            return response;
        }

        // TODO: Lấy CartItem và tính tổng
        response.setItems(new ArrayList<>());
        response.setGrandTotal(BigDecimal.ZERO);

        return response;
    }

    @Override
    @Transactional
    public void updateCartItemQuantity(String cartItemId, int quantity) {
        if (quantity <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy mục trong giỏ hàng"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void removeCartItem(String cartItemId) {
        if (!cartItemRepository.existsById(cartItemId)) {
            throw new RuntimeException("Không tìm thấy mục trong giỏ hàng");
        }
        cartItemRepository.deleteById(cartItemId);
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy giỏ hàng"));

        // Xóa tất cả CartItem
        cartItemRepository.deleteAllByCart_CartId(cart.getCartId());
    }
}