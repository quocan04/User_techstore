package com.teamforone.tech_store.service.user;

import java.util.*;
import com.teamforone.tech_store.model.Categories;
import com.teamforone.tech_store.model.Product;
import com.teamforone.tech_store.repository.admin.crud.CategoryRepository;
import com.teamforone.tech_store.repository.admin.crud.UserProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("userProductService")
public class UserProductServiceImpl implements UserProductService {

    @Autowired
    private UserProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Override
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    @Override
    public Product getProductById(String id) {
        return productRepository.findById(id).orElse(null);
    }

    @Override
    public Product getProductBySlug(String slug) {
        return productRepository.findBySlug(slug);
    }

    @Override
    public List<Product> getProductsByCategorySlug(String slug) {
        System.out.println("DEBUG: Nhận slug từ navbar = " + slug);

        Categories category = categoryRepository.findBySlug(slug);

        if (category == null) {
            System.out.println("DEBUG: Không tìm thấy category với slug = " + slug);
            return Collections.emptyList();
        }

        System.out.println("DEBUG: Tìm thấy category ID = " + category.getCategoryId());
        System.out.println("DEBUG: Category Name = " + category.getCategoryName());

        List<String> categoryIds = new ArrayList<>();
        categoryIds.add(category.getCategoryId());

        if (category.hasChildren()) {
            for (Categories subCategory : category.getSubCategories()) {
                categoryIds.add(subCategory.getCategoryId());
                System.out.println("DEBUG: Thêm subcategory ID = " + subCategory.getCategoryId() + " (" + subCategory.getCategoryName() + ")");
            }
        }

        System.out.println("DEBUG: Tìm kiếm products trong " + categoryIds.size() + " categories");

        List<Product> products = productRepository.findByCategoryIdIn(categoryIds);

        System.out.println("DEBUG: Tìm được " + products.size() + " sản phẩm");

        return products;
    }

    @Override
    public List<Product> searchProducts(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return productRepository.findAll();
        }
        return productRepository.findByNameContainingIgnoreCase(keyword);
    }

    @Override
    public List<Product> getBestSellersDaily() {
        System.out.println("Thực thi: Lấy sản phẩm bán chạy trong ngày.");
        return Collections.emptyList();
    }

    @Override
    public List<Product> getBestSellersMonthly() {
        System.out.println("Thực thi: Lấy sản phẩm bán chạy trong tháng.");
        return Collections.emptyList();
    }

    @Override
    public List<Product> getBestSellersQuarterly() {
        System.out.println("Thực thi: Lấy sản phẩm bán chạy trong quý.");
        return Collections.emptyList();
    }

    @Override
    public List<Product> getBestSellersYearly() {
        System.out.println("Thực thi: Lấy sản phẩm hot nhất năm.");
        return Collections.emptyList();
    }
}