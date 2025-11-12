    package com.teamforone.tech_store.repository.admin.crud;

    import org.springframework.data.repository.query.Param;
    import com.teamforone.tech_store.model.Product;
    import org.springframework.data.jpa.repository.JpaRepository;
    import org.springframework.stereotype.Repository;
    import org.springframework.data.jpa.repository.Query;

    import java.util.List;

    @Repository
    public interface UserProductRepository extends JpaRepository<Product, String> {

        @Query(value = "SELECT p.* FROM products p JOIN categories c ON p.category_id = c.category_id WHERE c.slug = :slug", nativeQuery = true)
        List<Product> findProductsByCategorySlug(@Param("slug") String slug);
    }

