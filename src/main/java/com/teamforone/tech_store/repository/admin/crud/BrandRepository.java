package com.teamforone.tech_store.repository.admin.crud;

import com.teamforone.tech_store.model.Brands;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandRepository extends JpaRepository<Brands, String> {
}
