package com.teamforone.tech_store.repository.admin.crud.user;

import com.teamforone.tech_store.model.Shipping;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShippingRepository extends JpaRepository<Shipping, String> {

    /**
     * Tìm tất cả Shipping theo userId
     * QUAN TRỌNG: Phải là User_UserId (vì trong User.java field là "userId")
     */
    List<Shipping> findByUser_Id(String userId);  // ← ĐỔI TỪ findByUserId
}