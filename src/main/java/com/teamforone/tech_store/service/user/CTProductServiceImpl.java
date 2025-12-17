package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.dto.ProductVariantDTO;
import com.teamforone.tech_store.model.CTProducts;
import com.teamforone.tech_store.model.Color;
import com.teamforone.tech_store.model.DisplaySize;
import com.teamforone.tech_store.model.Storage;
import com.teamforone.tech_store.repository.admin.crud.CTProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CTProductServiceImpl implements CTProductService {

    private static final Logger log = LoggerFactory.getLogger(CTProductServiceImpl.class);

    @Autowired
    private CTProductRepository ctProductRepository;

    // ===== Phần cũ (giữ nguyên) =====

    @Override
    public List<CTProducts> getVariantsByProductId(String productId) {
        log.debug("DEBUG: Tìm variants cho product_id = {}", productId);
        List<CTProducts> variants = ctProductRepository.findByProductId(productId);
        log.debug("DEBUG: Tìm thấy {} variants", variants.size());
        return variants;
    }

    @Override
    public CTProducts getCheapestVariant(String productId) {
        CTProducts variant = ctProductRepository.findFirstByProductIdOrderByPriceAsc(productId);
        if (variant != null) {
            log.debug("DEBUG: Giá rẻ nhất = {}", variant.getPrice());
        }
        return variant;
    }

    // ===== Phần mới (Product Variant Selection) =====

    @Override
    public ProductVariantDTO getProductVariants(String productId) {
        log.debug("🎨 Lấy danh sách màu sắc cho product_id = {}", productId);

        ProductVariantDTO variantDTO = new ProductVariantDTO();

        // Lấy danh sách màu sắc có sẵn (quantity > 0)
        List<Color> colors = ctProductRepository.findAvailableColorsByProductId(productId);

        log.debug("✅ Tìm thấy {} màu sắc có sẵn", colors.size());

        variantDTO.setColors(colors.stream()
                .map(color -> new ProductVariantDTO.VariantOption(
                        color.getColorID(),
                        color.getColorName(),
                        null, // Color entity không có colorCode, để null
                        true
                ))
                .collect(Collectors.toList()));

        return variantDTO;
    }

    @Override
    public List<ProductVariantDTO.VariantOption> getStoragesByColor(String productId, String colorId) {
        log.debug("💾 Lấy danh sách bộ nhớ cho product_id={}, color_id={}", productId, colorId);

        List<Storage> storages = ctProductRepository
                .findAvailableStoragesByProductIdAndColor(productId, colorId);

        log.debug("✅ Tìm thấy {} bộ nhớ có sẵn", storages.size());

        return storages.stream()
                .map(storage -> new ProductVariantDTO.VariantOption(
                        storage.getStorageID(),
                        storage.getRam() + " / " + storage.getRom(), // VD: "8GB / 128GB"
                        storage.getRam() + " / " + storage.getRom(),
                        true
                ))
                .collect(Collectors.toList());
    }

    @Override
    public List<ProductVariantDTO.VariantOption> getSizesByColorAndStorage(
            String productId, String colorId, String storageId) {

        log.debug("📐 Lấy danh sách size cho product_id={}, color_id={}, storage_id={}",
                productId, colorId, storageId);

        List<DisplaySize> sizes = ctProductRepository
                .findAvailableSizesByProductIdColorAndStorage(productId, colorId, storageId);

        log.debug("✅ Tìm thấy {} size có sẵn", sizes.size());

        return sizes.stream()
                .map(size -> new ProductVariantDTO.VariantOption(
                        size.getDisplaySizeID(),
                        size.getSizeInch() + "\"", // VD: "6.1""
                        size.getResolution(), // Độ phân giải
                        true
                ))
                .collect(Collectors.toList());
    }

    @Override
    public Optional<CTProducts> getVariantDetails(
            String productId, String colorId, String storageId, String sizeId) {

        log.debug("🔍 Lấy chi tiết variant: product={}, color={}, storage={}, size={}",
                productId, colorId, storageId, sizeId);

        Optional<CTProducts> variant = ctProductRepository
                .findVariantByAllIds(productId, colorId, storageId, sizeId);

        if (variant.isPresent()) {
            log.debug("✅ Tìm thấy variant - Giá: {}, Số lượng: {}",
                    variant.get().getPrice(), variant.get().getQuantity());
        } else {
            log.warn("⚠️ Không tìm thấy variant với các thuộc tính đã cho");
        }

        return variant;
    }

    @Override
    public boolean isVariantInStock(
            String productId, String colorId, String storageId, String sizeId) {

        boolean inStock = ctProductRepository.existsAndInStock(productId, colorId, storageId, sizeId);

        log.debug("📦 Variant {} hàng", inStock ? "CÒN" : "HẾT");

        return inStock;
    }

    @Override
    public Optional<Double> getVariantPrice(
            String productId, String colorId, String storageId, String sizeId) {

        Optional<Double> price = ctProductRepository
                .findPriceByVariant(productId, colorId, storageId, sizeId);

        if (price.isPresent()) {
            log.debug("💰 Giá variant: {}", price.get());
        } else {
            log.warn("⚠️ Không tìm thấy giá cho variant");
        }

        return price;
    }
}