package com.teamforone.tech_store.repository.admin;

import com.teamforone.tech_store.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.UUID; // Giữ lại nếu cần cho các phương thức khác, nhưng không cần thiết cho JpaRepository<User, String>

@Repository
// Sử dụng kiểu String cho ID để khớp với UUID/CHAR(36) trong Entity User
public interface UserRepository extends JpaRepository<User, String> {

    // Phương thức từ UserRepositoryl (được thêm vào)
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    // Phương thức findAll() và findById() được kế thừa từ JpaRepository<User, String>
}