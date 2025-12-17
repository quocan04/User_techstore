package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.model.Product;
import java.util.List;

public interface UserProductService {
    List<Product> getAllProducts();
    Product getProductById(String id);
    Product getProductBySlug(String slug);
    List<Product> getProductsByCategorySlug(String slug);
    List<Product> searchProducts(String keyword);
    List<Product> getBestSellersDaily();
    List<Product> getBestSellersMonthly();
    List<Product> getBestSellersQuarterly();
    List<Product> getBestSellersYearly();
}