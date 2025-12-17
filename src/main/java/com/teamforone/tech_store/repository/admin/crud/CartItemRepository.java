package com.teamforone.tech_store.repository.admin.crud;

import com.teamforone.tech_store.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.UUID;
import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, UUID> {

    Optional<CartItem> findByCart_CartIdAndProductIdAndColorIdAndSizeIdAndStorageId(
            UUID cartId, UUID productId, UUID colorId, UUID sizeId, UUID storageId
    );

    @Modifying
    @Query("DELETE FROM CartItem ci WHERE ci.cart.cartId = :cartId")
    void deleteAllByCart_CartId(@Param("cartId") UUID cartId);

    // HÀM 1: Để Spring tự xử lý (KHÔNG để @Query ở đây)
    List<CartItem> findByCart_CartId(UUID cartId);

    // HÀM 2: Dùng Native Query (PHẢI đặt @Query ở đây)
    @Query(
            value = """
        SELECT 
            ci.item_id,
            ci.product_id,
            p.name,
            p.default_image,
            ci.quantity,
            (SELECT COALESCE(sale_price, price) 
             FROM ctproducts 
             WHERE product_id = ci.product_id 
             LIMIT 1) AS unit_price,
            (ci.quantity * (SELECT COALESCE(sale_price, price) 
                            FROM ctproducts 
                            WHERE product_id = ci.product_id 
                            LIMIT 1)) AS subtotal,
            c.colorname,
            CONCAT(s.ram, ' / ', s.rom) AS storage_name,
            CONCAT(sz.size_inch, '"') AS size_name
        FROM cart_items ci
        INNER JOIN products p ON ci.product_id = p.product_id
        LEFT JOIN color c ON ci.colorid = c.colorID
        LEFT JOIN storage s ON ci.storageid = s.storageID
        LEFT JOIN displaysize sz ON ci.sizeid = sz.sizeID
        WHERE ci.cart_id = :cartId
        """
            , nativeQuery = true
    )
    List<Object[]> findCartItemsWithDetailsNative(@Param("cartId") UUID cartId);
}