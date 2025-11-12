package com.teamforone.tech_store.repository.admin;

import com.teamforone.tech_store.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AddressRepository extends JpaRepository<Address, Long> {
}