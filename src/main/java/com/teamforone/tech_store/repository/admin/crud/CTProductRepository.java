package com.teamforone.tech_store.repository.admin.crud;

import com.teamforone.tech_store.model.CTProducts;
import com.teamforone.tech_store.model.CTProductId;
import com.teamforone.tech_store.model.Color;
import com.teamforone.tech_store.model.Storage;
import com.teamforone.tech_store.model.DisplaySize;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CTProductRepository extends JpaRepository<CTProducts, CTProductId> {

    // ===== PHẦN CŨ (GIỮ NGUYÊN) =====

    // Lấy tất cả variants của 1 sản phẩm
    List<CTProducts> findByProductId(String productId);

    // Lấy variant có giá thấp nhất (giá rẻ nhất)
    CTProducts findFirstByProductIdOrderByPriceAsc(String productId);

    // Lấy variant có giá cao nhất
    CTProducts findFirstByProductIdOrderByPriceDesc(String productId);

    // Lấy tất cả màu sắc có sẵn của sản phẩm (distinct)
    @Query("SELECT DISTINCT ct.color FROM CTProducts ct WHERE ct.productId = :productId")
    List<Color> findDistinctColorsByProductId(@Param("productId") String productId);

    // Lấy tất cả dung lượng có sẵn của sản phẩm (distinct)
    @Query("SELECT DISTINCT ct.storage FROM CTProducts ct WHERE ct.productId = :productId")
    List<Storage> findDistinctStoragesByProductId(@Param("productId") String productId);

    // Lấy tất cả kích thước có sẵn (distinct)
    @Query("SELECT DISTINCT ct.size FROM CTProducts ct WHERE ct.productId = :productId")
    List<DisplaySize> findDistinctSizesByProductId(@Param("productId") String productId);

    // Tìm variant cụ thể theo các thuộc tính
    CTProducts findByProductIdAndColorIdAndStorageIdAndSizeId(
            String productId,
            String colorId,
            String storageId,
            String sizeId
    );

    // Lấy variants theo productId và colorId
    List<CTProducts> findByProductIdAndColorId(String productId, String colorId);

    // Lấy variants theo productId và storageId
    List<CTProducts> findByProductIdAndStorageId(String productId, String storageId);

    // Thêm dòng này vào interface CTProductRepository
    Optional<CTProducts> findFirstByProductId(String productId);

    // Đếm số lượng variants của 1 sản phẩm
    long countByProductId(String productId);

    // ===== PHẦN MỚI (VARIANT SELECTION WITH STOCK CHECK) =====

    // Lấy các màu sắc CÓ SẴN (quantity > 0) cho sản phẩm
    @Query("SELECT DISTINCT c FROM CTProducts ct " +
            "JOIN ct.color c " +
            "WHERE ct.productId = :productId " +
            "AND ct.quantity > 0 " +
            "ORDER BY c.colorName")
    List<Color> findAvailableColorsByProductId(@Param("productId") String productId);

    // Lấy các bộ nhớ CÓ SẴN theo sản phẩm và màu
    @Query("SELECT DISTINCT s FROM CTProducts ct " +
            "JOIN ct.storage s " +
            "WHERE ct.productId = :productId " +
            "AND ct.colorId = :colorId " +
            "AND ct.quantity > 0 " +
            "ORDER BY s.rom")
    List<Storage> findAvailableStoragesByProductIdAndColor(
            @Param("productId") String productId,
            @Param("colorId") String colorId
    );

    // Lấy các size CÓ SẴN theo sản phẩm, màu và bộ nhớ
    @Query("SELECT DISTINCT ds FROM CTProducts ct " +
            "JOIN ct.size ds " +
            "WHERE ct.productId = :productId " +
            "AND ct.colorId = :colorId " +
            "AND ct.storageId = :storageId " +
            "AND ct.quantity > 0 " +
            "ORDER BY ds.sizeInch")
    List<DisplaySize> findAvailableSizesByProductIdColorAndStorage(
            @Param("productId") String productId,
            @Param("colorId") String colorId,
            @Param("storageId") String storageId
    );

    // Tìm variant cụ thể với Optional (thay thế findByProductIdAndColorIdAndStorageIdAndSizeId)
    @Query("SELECT ct FROM CTProducts ct " +
            "WHERE ct.productId = :productId " +
            "AND ct.colorId = :colorId " +
            "AND ct.storageId = :storageId " +
            "AND ct.sizeId = :sizeId")
    Optional<CTProducts> findVariantByAllIds(
            @Param("productId") String productId,
            @Param("colorId") String colorId,
            @Param("storageId") String storageId,
            @Param("sizeId") String sizeId
    );

    // Kiểm tra variant có tồn tại và còn hàng không
    @Query("SELECT CASE WHEN COUNT(ct) > 0 THEN true ELSE false END " +
            "FROM CTProducts ct " +
            "WHERE ct.productId = :productId " +
            "AND ct.colorId = :colorId " +
            "AND ct.storageId = :storageId " +
            "AND ct.sizeId = :sizeId " +
            "AND ct.quantity > 0")
    boolean existsAndInStock(
            @Param("productId") String productId,
            @Param("colorId") String colorId,
            @Param("storageId") String storageId,
            @Param("sizeId") String sizeId
    );

    // Lấy giá của variant cụ thể (ưu tiên salePrice)
    @Query("SELECT COALESCE(ct.salePrice, ct.price) FROM CTProducts ct " +
            "WHERE ct.productId = :productId " +
            "AND ct.colorId = :colorId " +
            "AND ct.storageId = :storageId " +
            "AND ct.sizeId = :sizeId " +
            "AND ct.quantity > 0")
    Optional<Double> findPriceByVariant(
            @Param("productId") String productId,
            @Param("colorId") String colorId,
            @Param("storageId") String storageId,
            @Param("sizeId") String sizeId
    );
}