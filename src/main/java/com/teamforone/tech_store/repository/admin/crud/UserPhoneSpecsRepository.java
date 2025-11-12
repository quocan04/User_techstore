package com.teamforone.tech_store.repository.admin.crud;

import com.teamforone.tech_store.model.PhoneSpecs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserPhoneSpecsRepository extends JpaRepository<PhoneSpecs, String> {
    Optional<PhoneSpecs> findByProductID(String productID);
}
