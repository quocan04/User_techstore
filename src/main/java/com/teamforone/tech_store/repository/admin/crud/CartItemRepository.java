package com.teamforone.tech_store.repository.admin.crud;

import com.teamforone.tech_store.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {

    void deleteAllByCart_CartID(String cartId);

    @Query(value = "SELECT * FROM cart_items ci " +
            "WHERE ci.cart_id = :cartId " +
            "AND ci.product_id = :productId " +
            "AND ci.colorID = :colorId " +
            "AND ci.sizeID = :sizeId " +
            "AND ci.storageID = :storageId " +
            "LIMIT 1",
            nativeQuery = true)
    Optional<CartItem> findByCart_CartIDAndProductAndColorAndDisplaySizeAndStorage(
            @Param("cartId") String cartId,
            @Param("productId") String productId,
            @Param("colorId") String colorId,
            @Param("sizeId") String sizeId,
            @Param("storageId") String storageId
    );

    @Query(value = "SELECT " +
            "ci.item_id, " +
            "ci.product_id, " +
            "p.name, " +
            "p.default_image, " +
            "ci.quantity, " +
            "ct.sale_price, " +
            "(ci.quantity * ct.sale_price) AS subtotal, " +
            "c.colorname, " +
            "CONCAT(s.ram, ' / ', s.rom) AS storage_info, " +
            "CONCAT(ds.size_inch, '\"') AS size_info " +
            "FROM cart_items ci " +
            "JOIN products p ON ci.product_id = p.product_id " +
            "LEFT JOIN color c ON ci.colorID = c.colorID " +
            "LEFT JOIN storage s ON ci.storageID = s.storageID " +
            "LEFT JOIN displaysize ds ON ci.sizeID = ds.sizeID " +
            "LEFT JOIN ctproducts ct ON ci.product_id = ct.product_id " +
            "   AND ci.colorID = ct.colorID " +
            "   AND ci.sizeID = ct.sizeID " +
            "   AND ci.storageID = ct.storageID " +
            "WHERE ci.cart_id = :cartId",
            nativeQuery = true)
    List<Object[]> findCartItemsWithDetailsNative(@Param("cartId") String cartId);
}