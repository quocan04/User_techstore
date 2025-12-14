package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.service.user.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collections;
import java.util.List;

@Controller
@RequestMapping("/stats") // Đường dẫn gốc cho tất cả các thống kê
public class StatsController {

    private final ProductService productService;

    // Sử dụng @RequiredArgsConstructor hoặc @Autowired cho constructor để tiêm ProductService
    @Autowired
    public StatsController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/best-sellers/{timePeriod}")
    public String showBestSellers(@PathVariable String timePeriod, Model model) {

        List<Product> bestSellers = Collections.emptyList();
        String statsTitle;

        switch (timePeriod.toLowerCase()) {
            case "daily":
                bestSellers = productService.getBestSellersDaily(); // Cần định nghĩa trong Service
                statsTitle = "Sản phẩm bán chạy trong ngày";
                break;
            case "monthly":
                bestSellers = productService.getBestSellersMonthly(); // Cần định nghĩa trong Service
                statsTitle = "Sản phẩm bán chạy trong tháng";
                break;
            case "quarterly":
                bestSellers = productService.getBestSellersQuarterly(); // Cần định nghĩa trong Service
                statsTitle = "Sản phẩm bán chạy trong quý";
                break;
            case "yearly":
                bestSellers = productService.getBestSellersYearly(); // Cần định nghĩa trong Service
                statsTitle = "Sản phẩm hot nhất năm";
                break;
            default:
                statsTitle = "Thống kê không hợp lệ";
                break;
        }

        model.addAttribute("products", bestSellers);
        model.addAttribute("statsTitle", statsTitle);

        return "user/home";
    }
}