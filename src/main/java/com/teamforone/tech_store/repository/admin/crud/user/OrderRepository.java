package com.teamforone.tech_store.repository.admin.crud.user;

import com.teamforone.tech_store.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Orders, String> {

    /**
     * Tìm tất cả đơn hàng của một user
     */
    List<Orders> findByUser_Id(String userId);
}