package com.teamforone.tech_store.service.user;

import com.teamforone.tech_store.dto.ProductVariantDTO;
import com.teamforone.tech_store.model.CTProducts;

import java.util.List;
import java.util.Optional;

public interface CTProductService {

    // ===== Phần cũ (giữ nguyên) =====

    /**
     * Lấy tất cả variants của sản phẩm
     */
    List<CTProducts> getVariantsByProductId(String productId);

    /**
     * Lấy variant có giá rẻ nhất
     */
    CTProducts getCheapestVariant(String productId);

    // ===== Phần mới (Product Variant Selection) =====

    /**
     * Lấy tất cả variants ban đầu (màu sắc) của sản phẩm
     * @param productId ID sản phẩm
     * @return DTO chứa danh sách màu sắc
     */
    ProductVariantDTO getProductVariants(String productId);

    /**
     * Lấy danh sách bộ nhớ có sẵn theo màu đã chọn
     * @param productId ID sản phẩm
     * @param colorId ID màu đã chọn
     * @return Danh sách bộ nhớ
     */
    List<ProductVariantDTO.VariantOption> getStoragesByColor(String productId, String colorId);

    /**
     * Lấy danh sách size có sẵn theo màu và bộ nhớ đã chọn
     * @param productId ID sản phẩm
     * @param colorId ID màu đã chọn
     * @param storageId ID bộ nhớ đã chọn
     * @return Danh sách size
     */
    List<ProductVariantDTO.VariantOption> getSizesByColorAndStorage(
            String productId, String colorId, String storageId
    );

    /**
     * Lấy thông tin chi tiết của variant cụ thể
     * @param productId ID sản phẩm
     * @param colorId ID màu
     * @param storageId ID bộ nhớ
     * @param sizeId ID size
     * @return Variant chi tiết
     */
    Optional<CTProducts> getVariantDetails(
            String productId, String colorId, String storageId, String sizeId
    );

    /**
     * Kiểm tra variant có còn hàng không
     * @param productId ID sản phẩm
     * @param colorId ID màu
     * @param storageId ID bộ nhớ
     * @param sizeId ID size
     * @return true nếu còn hàng
     */
    boolean isVariantInStock(
            String productId, String colorId, String storageId, String sizeId
    );

    /**
     * Lấy giá của variant (ưu tiên salePrice)
     * @param productId ID sản phẩm
     * @param colorId ID màu
     * @param storageId ID bộ nhớ
     * @param sizeId ID size
     * @return Giá sản phẩm
     */
    Optional<Double> getVariantPrice(
            String productId, String colorId, String storageId, String sizeId
    );
}