package com.teamforone.tech_store.service.user.impl;

import com.teamforone.tech_store.dto.request.AddToCartRequest;
import com.teamforone.tech_store.dto.request.CheckoutRequest;
import com.teamforone.tech_store.dto.response.CartItemResponse;
import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.dto.response.PaymentResponse;
import com.teamforone.tech_store.model.Cart;
import com.teamforone.tech_store.model.CartItem;
import com.teamforone.tech_store.model.User;
import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.repository.admin.crud.CartItemRepository;
import com.teamforone.tech_store.repository.admin.crud.CartRepository;
import com.teamforone.tech_store.service.user.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.teamforone.tech_store.repository.admin.UserRepository;
import com.teamforone.tech_store.repository.admin.crud.UserProductRepository;
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
    private final UserProductRepository userProductRepository; // ✅ THÊM DÒNG NÀY

    @Autowired
    public CartServiceImpl(CartRepository cartRepository,
                           CartItemRepository cartItemRepository,
                           UserRepository userRepository,
                           UserProductRepository userProductRepository) { // ✅ THÊM PARAMETER
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.userRepository = userRepository;
        this.userProductRepository = userProductRepository; // ✅ THÊM DÒNG NÀY
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
        // 1. Tìm hoặc tạo Cart cho user
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User không tồn tại"));

        Cart cart = cartRepository.findByUser_Id(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUser(user);
                    return cartRepository.save(newCart);
                });

        // 2. Tìm sản phẩm
        Product product = userProductRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Sản phẩm không tồn tại"));

        // 3. ✅ SỬA LẠI: Kiểm tra xem sản phẩm đã có trong cart chưa (với variant)
        // Vì CartItem dùng String product, không phải @ManyToOne, nên ta query theo String
        Optional<CartItem> existingItem = cartItemRepository.findByCart_CartIDAndProductAndColorAndDisplaySizeAndStorage(
                cart.getCartID(),
                request.getProductId(),
                request.getColorId(),
                request.getSizeId(),
                request.getStorageId()
        );

        if (existingItem.isPresent()) {
            // Nếu đã có thì tăng số lượng
            CartItem item = existingItem.get();
            item.setQuantity(item.getQuantity() + request.getQuantity());
            cartItemRepository.save(item);
        } else {
            // Nếu chưa có thì tạo mới
            CartItem newItem = new CartItem();
            newItem.setCart(cart);
            newItem.setProduct(request.getProductId()); // ✅ Set String productId
            newItem.setColor(request.getColorId());
            newItem.setDisplaySize(request.getSizeId());
            newItem.setStorage(request.getStorageId());
            newItem.setQuantity(request.getQuantity());
            cartItemRepository.save(newItem);
        }
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
    public void updateCartItemQuantity(String cartItemId, int quantity) {
        // 1. Tìm CartItem
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem không tồn tại"));

        // 2. Validate số lượng
        if (quantity <= 0) {
            throw new RuntimeException("Số lượng phải lớn hơn 0");
        }

        // 3. Cập nhật số lượng
        cartItem.setQuantity(quantity);
        cartItemRepository.save(cartItem);
    }

    @Override
    @Transactional
    public void removeCartItem(String cartItemId) {
        // 1. Tìm CartItem
        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("CartItem không tồn tại"));

        // 2. Xóa
        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        // 1. Tìm Cart của user
        Optional<Cart> cartOpt = cartRepository.findByUser_Id(userId);

        if (cartOpt.isPresent()) {
            Cart cart = cartOpt.get();

            // 2. ✅ SỬA LẠI: Xóa tất cả CartItem trong cart
            cartItemRepository.deleteAllByCart_CartID(cart.getCartID());
        }
    }

    // Helper method: Convert Object[] từ native query sang CartItemResponse
    private CartItemResponse convertToCartItemResponse(Object[] row) {
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