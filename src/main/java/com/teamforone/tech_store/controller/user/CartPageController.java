package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.dto.response.CartResponse;
import com.teamforone.tech_store.service.user.CartService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.util.ArrayList;

@Controller
@RequestMapping("/cart")
public class CartPageController {

    @Autowired
    private CartService cartService;

    @GetMapping
    public String viewCart(HttpSession session, Model model) {
        String userId = (String) session.getAttribute("userId");

        if (userId == null) {
            return "redirect:/auth/dangnhap";
        }

        CartResponse cartResponse = null;

        try {
            cartResponse = cartService.getCartByUserId(userId);
        } catch (Exception e) {
            System.err.println("❌ Lỗi khi tải giỏ hàng: " + e.getMessage());
            e.printStackTrace();
        }

        // ✅ CRITICAL: Luôn tạo cart object, không bao giờ null
        if (cartResponse == null) {
            cartResponse = new CartResponse();
            cartResponse.setItems(new ArrayList<>());
            cartResponse.setTotalItems(0);
            cartResponse.setTemporaryTotal(BigDecimal.ZERO);
            cartResponse.setGrandTotal(BigDecimal.ZERO);
        }

        // ✅ Đảm bảo items không null
        if (cartResponse.getItems() == null) {
            cartResponse.setItems(new ArrayList<>());
        }

        // ✅ Đảm bảo totalItems không null
        if (cartResponse.getTotalItems() == null) {
            cartResponse.setTotalItems(0);
        }

        // ✅ Đảm bảo temporaryTotal không null
        if (cartResponse.getTemporaryTotal() == null) {
            cartResponse.setTemporaryTotal(BigDecimal.ZERO);
        }

        // ✅ Đảm bảo grandTotal không null
        if (cartResponse.getGrandTotal() == null) {
            cartResponse.setGrandTotal(BigDecimal.ZERO);
        }

        model.addAttribute("cart", cartResponse);
        return "user/cart";
    }
}