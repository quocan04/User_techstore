package com.teamforone.tech_store.repository.admin.crud;


import com.teamforone.tech_store.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, String> {

    /**
     * Tìm Cart theo userId
     * Spring Data JPA tự động tạo query:
     * SELECT * FROM cart WHERE user_id = ?
     */
    Optional<Cart> findByUser_Id(String userId);
}