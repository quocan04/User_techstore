package com.teamforone.tech_store.service.user.impl;

import com.teamforone.tech_store.dto.request.AddToCartRequest;
import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.dto.response.CartItemResponse;
import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.dto.response.PaymentResponse;
import com.teamforone.tech_store.model.Cart;
import com.teamforone.tech_store.model.CartItem;
import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.model.User;
import com.teamforone.tech_store.repository.admin.UserRepository;
import com.teamforone.tech_store.repository.admin.crud.CartItemRepository;
import com.teamforone.tech_store.repository.admin.crud.CartRepository;
import com.teamforone.tech_store.repository.admin.crud.UserProductRepository;
import com.teamforone.tech_store.service.user.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.nio.ByteBuffer; // ⬅️ Bổ sung Import này
import java.util.*;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final UserProductRepository userProductRepository;

    @Autowired
    public CartServiceImpl(
            CartRepository cartRepository,
            CartItemRepository cartItemRepository,
            UserRepository userRepository,
            UserProductRepository userProductRepository
    ) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.userProductRepository = userProductRepository;
    }

    // ======================================================
    // CHECKOUT
    // ======================================================
    @Override
    @Transactional
    public PaymentResponse processVnpayCheckout(String userId, CheckoutRequest request) {

        CartResponse cartData = getCartByUserId(userId);

        if (cartData.getTotalItems() == 0) {
            throw new RuntimeException("Giỏ hàng trống, không thể thanh toán.");
        }

        String orderNo = "ORD-" + System.currentTimeMillis();
        BigDecimal amount = cartData.getGrandTotal();

        PaymentResponse response = new PaymentResponse();
        response.setOrderNo(orderNo);
        response.setPaymentUrl("VNPAY_URL_" + amount);

        return response;
    }

    // ======================================================
    // ADD TO CART
    // ======================================================
    @Override
    @Transactional
    public void addToCart(String userId, AddToCartRequest request) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        // Lấy hoặc tạo cart
        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    Cart savedCart = cartRepository.save(newCart);
                    cartRepository.flush(); // ⬅️ FORCE FLUSH NGAY
                    return savedCart;
                });

        // Đảm bảo cart đã có trong DB
        if (cart.getCartId() == null) {
            throw new RuntimeException("Cart ID is null after save");
        }

        // Validate product
        String pidStr = String.valueOf(request.getProductId());

        userProductRepository.findById(pidStr)
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        UUID cartId = cart.getCartId();
        UUID productId = request.getProductId();
        UUID colorId = request.getColorId();
        UUID sizeId = request.getSizeId();
        UUID storageId = request.getStorageId();

        // Debug log
        System.out.println("🔥 Attempting to insert CartItem with cart_id: " + cartId);

        Optional<CartItem> existingItem =
                cartItemRepository.findByCart_CartIdAndProductIdAndColorIdAndSizeIdAndStorageId(
                        cartId, productId, colorId, sizeId, storageId);

        if (existingItem.isPresent()) {
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
            return;
        }

        CartItem newItem = new CartItem();
        newItem.setCart(cart); // ⬅️ Set relationship, không chỉ set ID
        newItem.setProductId(productId);
        newItem.setColorId(colorId);
        newItem.setSizeId(sizeId);
        newItem.setStorageId(storageId);
        newItem.setQuantity(request.getQuantity());

        cartItemRepository.saveAndFlush(newItem); // ⬅️ Dùng saveAndFlush
    }

    // ======================================================
    // GET CART
    // ======================================================
    @Override
    public CartResponse getCartByUserId(String userId) {

        System.out.println("🔥 getCartByUserId - userId: " + userId);

        Optional<Cart> cartOpt = cartRepository.findByUser_Id(userId);

        System.out.println("🔥 Cart found: " + cartOpt.isPresent());

        CartResponse response = new CartResponse();

        if (cartOpt.isEmpty()) {
            response.setCartId(null);
            response.setItems(Collections.emptyList());
            response.setTotalItems(0);
            response.setTemporaryTotal(BigDecimal.ZERO);
            response.setGrandTotal(BigDecimal.ZERO);
            return response;
        }

        Cart cart = cartOpt.get();

        System.out.println("🔥 Cart ID: " + cart.getCartId());

        List<Object[]> rawResults =
                cartItemRepository.findCartItemsWithDetailsNative(cart.getCartId());

        System.out.println("🔥 Raw results size: " + rawResults.size());

        List<CartItemResponse> items = rawResults.stream()
                .map(this::convertToCartItemResponse)
                .collect(Collectors.toList());

        System.out.println("🔥 Converted items size: " + items.size());

        int totalItems = items.stream()
                .mapToInt(CartItemResponse::getQuantity)
                .sum();

        BigDecimal temporaryTotal = items.stream()
                .map(CartItemResponse::getSubtotal)
                .filter(Objects::nonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        response.setCartId(cart.getCartId().toString());
        response.setItems(items);
        response.setTotalItems(totalItems);
        response.setTemporaryTotal(temporaryTotal);
        response.setGrandTotal(temporaryTotal);

        System.out.println("🔥 Final response - totalItems: " + totalItems);
        System.out.println("🔥 Final response - grandTotal: " + temporaryTotal);

        return response;
    }

    // ======================================================
    // UPDATE QUANTITY
    // ======================================================
    @Override
    public void updateCartItemQuantity(String cartItemId, int quantity) {

        if (quantity <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        UUID itemId = UUID.fromString(cartItemId);

        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new RuntimeException("CartItem không tồn tại"));

        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    // ======================================================
    // REMOVE ITEM
    // ======================================================
    @Override
    @Transactional
    public void removeCartItem(String cartItemId) {
        try {
            UUID itemId = UUID.fromString(cartItemId);
            CartItem cartItem = cartItemRepository.findById(itemId)
                    .orElseThrow(() -> new RuntimeException("CartItem không tồn tại"));
            cartItemRepository.delete(cartItem);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("cartItemId không hợp lệ: " + cartItemId);
        }
    }


    // ======================================================
    // CLEAR CART
    // ======================================================
    @Override
    @Transactional
    public void clearCart(String userId) {

        cartRepository.findByUser_Id(userId)
                .ifPresent(cart ->
                        cartItemRepository.deleteAllByCart_CartId(cart.getCartId())
                );
    }

    // ======================================================
    // MAPPER
    // ======================================================
    private CartItemResponse convertToCartItemResponse(Object[] row) {

        CartItemResponse response = new CartItemResponse();

        // Sử dụng convertIdToString để xử lý lỗi [B@... (byte array)
        response.setCartItemId(convertIdToString(row[0]));
        response.setProductId(convertIdToString(row[1]));

        response.setProductName((String) row[2]);
        response.setDefaultImage((String) row[3]);
        response.setQuantity(((Number) row[4]).intValue());

        // Sử dụng toBigDecimal để xử lý lỗi "cannot find symbol"
        response.setUnitPrice(toBigDecimal(row[5]));
        response.setSubtotal(toBigDecimal(row[6]));

        response.setColorName((String) row[7]);
        response.setStorageName((String) row[8]);
        response.setSizeName((String) row[9]);

        return response;
    }

    // ======================================================
    // HELPER METHODS
    // ======================================================

    private String convertIdToString(Object obj) {
        if (obj == null) return null;
        if (obj instanceof String) return (String) obj;
        if (obj instanceof UUID) return obj.toString();
        if (obj instanceof byte[]) {
            byte[] bytes = (byte[]) obj;
            if (bytes.length == 16) {
                ByteBuffer bb = ByteBuffer.wrap(bytes);
                return new UUID(bb.getLong(), bb.getLong()).toString();
            }
        }
        return obj.toString();
    }

    private BigDecimal toBigDecimal(Object value) {
        if (value == null) {
            return BigDecimal.ZERO;
        }
        if (value instanceof BigDecimal) {
            return (BigDecimal) value;
        }
        if (value instanceof Number) {
            return BigDecimal.valueOf(((Number) value).doubleValue());
        }
        try {
            return new BigDecimal(value.toString());
        } catch (Exception e) {
            return BigDecimal.ZERO;
        }
    }
}