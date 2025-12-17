package com.teamforone.tech_store.controller.user;

import com.teamforone.tech_store.dto.ProductVariantDTO;
import com.teamforone.tech_store.model.CTProducts;
import com.teamforone.tech_store.service.user.CTProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/products")
public class ProductVariantController {

    private static final Logger log = LoggerFactory.getLogger(ProductVariantController.class);

    @Autowired
    private CTProductService ctProductService;

    /**
     * API: Lấy danh sách màu sắc có sẵn của sản phẩm
     * GET /api/products/{productId}/variants
     */
    @GetMapping("/{productId}/variants")
    public ResponseEntity<ProductVariantDTO> getProductVariants(@PathVariable String productId) {
        log.info("📞 API called: GET /api/products/{}/variants", productId);

        try {
            ProductVariantDTO variants = ctProductService.getProductVariants(productId);

            if (variants.getColors() == null || variants.getColors().isEmpty()) {
                log.warn("⚠️ Không tìm thấy màu sắc nào cho product_id = {}", productId);
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(variants);

        } catch (Exception e) {
            log.error("❌ Lỗi khi lấy variants: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * API: Lấy danh sách bộ nhớ theo màu đã chọn
     * GET /api/products/{productId}/storages?colorId=xxx
     */
    @GetMapping("/{productId}/storages")
    public ResponseEntity<List<ProductVariantDTO.VariantOption>> getStoragesByColor(
            @PathVariable String productId,
            @RequestParam String colorId) {

        log.info("📞 API called: GET /api/products/{}/storages?colorId={}", productId, colorId);

        try {
            List<ProductVariantDTO.VariantOption> storages =
                    ctProductService.getStoragesByColor(productId, colorId);

            if (storages.isEmpty()) {
                log.warn("⚠️ Không tìm thấy bộ nhớ nào");
                return ResponseEntity.noContent().build();
            }

            return ResponseEntity.ok(storages);

        } catch (Exception e) {
            log.error("❌ Lỗi khi lấy storages: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * API: Lấy danh sách size theo màu và bộ nhớ đã chọn
     * GET /api/products/{productId}/sizes?colorId=xxx&storageId=yyy
     */
    @GetMapping("/{productId}/sizes")
    public ResponseEntity<List<ProductVariantDTO.VariantOption>> getSizesByColorAndStorage(
            @PathVariable String productId,
            @RequestParam String colorId,
            @RequestParam String storageId) {

        log.info("📞 API called: GET /api/products/{}/sizes?colorId={}&storageId={}",
                productId, colorId, storageId);

        try {
            List<ProductVariantDTO.VariantOption> sizes =
                    ctProductService.getSizesByColorAndStorage(productId, colorId, storageId);

            // Nếu không có size, trả về empty list (sản phẩm không có thuộc tính size)
            return ResponseEntity.ok(sizes);

        } catch (Exception e) {
            log.error("❌ Lỗi khi lấy sizes: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * API: Lấy thông tin chi tiết variant (giá, số lượng)
     * GET /api/products/{productId}/variant-details?colorId=xxx&storageId=yyy&sizeId=zzz
     */
    @GetMapping("/{productId}/variant-details")
    public ResponseEntity<Map<String, Object>> getVariantDetails(
            @PathVariable String productId,
            @RequestParam String colorId,
            @RequestParam String storageId,
            @RequestParam(required = false, defaultValue = "default") String sizeId) {

        log.info("📞 API called: GET /api/products/{}/variant-details", productId);

        try {
            Optional<CTProducts> variantOpt = ctProductService
                    .getVariantDetails(productId, colorId, storageId, sizeId);

            if (variantOpt.isEmpty()) {
                log.warn("⚠️ Không tìm thấy variant");
                return ResponseEntity.notFound().build();
            }

            CTProducts variant = variantOpt.get();

            Map<String, Object> response = new HashMap<>();
            response.put("price", variant.getPrice());
            response.put("salePrice", variant.getSalePrice());
            response.put("quantity", variant.getQuantity());
            response.put("inStock", variant.getQuantity() > 0);

            // Giá hiển thị (ưu tiên salePrice)
            Double displayPrice = variant.getSalePrice() != null
                    ? variant.getSalePrice()
                    : variant.getPrice();
            response.put("displayPrice", displayPrice);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Lỗi khi lấy variant details: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * API: Kiểm tra variant có còn hàng không
     * GET /api/products/{productId}/check-stock?colorId=xxx&storageId=yyy&sizeId=zzz
     */
    @GetMapping("/{productId}/check-stock")
    public ResponseEntity<Map<String, Boolean>> checkStock(
            @PathVariable String productId,
            @RequestParam String colorId,
            @RequestParam String storageId,
            @RequestParam(required = false, defaultValue = "default") String sizeId) {

        log.info("📞 API called: GET /api/products/{}/check-stock", productId);

        try {
            boolean inStock = ctProductService
                    .isVariantInStock(productId, colorId, storageId, sizeId);

            Map<String, Boolean> response = new HashMap<>();
            response.put("inStock", inStock);

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            log.error("❌ Lỗi khi kiểm tra stock: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}