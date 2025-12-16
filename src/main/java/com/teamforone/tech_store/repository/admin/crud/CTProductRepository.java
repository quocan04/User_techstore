package com.teamforone.tech_store.repository.admin.crud;

import com.teamforone.tech_store.model.CTProducts;
import com.teamforone.tech_store.model.CTProductId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CTProductRepository extends JpaRepository<CTProducts, CTProductId> {

    // Lấy tất cả variants của 1 sản phẩm
    List<CTProducts> findByProductId(String productId);

    // Lấy variant có giá thấp nhất (giá rẻ nhất)
    CTProducts findFirstByProductIdOrderByPriceAsc(String productId);

    // Lấy variant có giá cao nhất
    CTProducts findFirstByProductIdOrderByPriceDesc(String productId);

    // Lấy tất cả màu sắc có sẵn của sản phẩm (distinct)
    @Query("SELECT DISTINCT ct.color FROM CTProducts ct WHERE ct.productId = :productId")
    List<com.teamforone.tech_store.model.Color> findDistinctColorsByProductId(@Param("productId") String productId);

    // Lấy tất cả dung lượng có sẵn của sản phẩm (distinct)
    @Query("SELECT DISTINCT ct.storage FROM CTProducts ct WHERE ct.productId = :productId")
    List<com.teamforone.tech_store.model.Storage> findDistinctStoragesByProductId(@Param("productId") String productId);

    // Lấy tất cả kích thước có sẵn (distinct)
    @Query("SELECT DISTINCT ct.size FROM CTProducts ct WHERE ct.productId = :productId")
    List<com.teamforone.tech_store.model.DisplaySize> findDistinctSizesByProductId(@Param("productId") String productId);

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

    // Đếm số lượng variants của 1 sản phẩm
    long countByProductId(String productId);
}