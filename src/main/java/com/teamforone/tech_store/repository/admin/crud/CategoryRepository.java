package com.teamforone.tech_store.repository.admin.crud;

import com.teamforone.tech_store.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Categories, String> {
}
