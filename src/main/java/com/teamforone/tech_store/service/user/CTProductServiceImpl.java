package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.model.CTProducts;
import com.teamforone.tech_store.repository.admin.crud.CTProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class CTProductServiceImpl implements CTProductService {

    @Autowired
    private CTProductRepository ctProductRepository;

    @Override
    public List<CTProducts> getVariantsByProductId(String productId) {
        System.out.println("DEBUG: Tìm variants cho product_id = " + productId);
        List<CTProducts> variants = ctProductRepository.findByProductId(productId);
        System.out.println("DEBUG: Tìm thấy " + variants.size() + " variants");
        return variants;
    }

    @Override
    public CTProducts getCheapestVariant(String productId) {
        CTProducts variant = ctProductRepository.findFirstByProductIdOrderByPriceAsc(productId);
        if (variant != null) {
            System.out.println("DEBUG: Giá rẻ nhất = " + variant.getPrice());
        }
        return variant;
    }
}