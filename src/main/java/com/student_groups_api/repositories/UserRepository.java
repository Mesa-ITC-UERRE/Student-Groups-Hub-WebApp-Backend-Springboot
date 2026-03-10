package com.student_groups_api.repositories;

import com.student_groups_api.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEntraOid(String entraOid);
    Optional<User> findByEmailIgnoreCase(String email);
    boolean existsByEntraOid(String entraOid);
    boolean existsByEmailIgnoreCase(String email);
}