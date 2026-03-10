package com.student_groups_api.repositories;

import com.student_groups_api.model.Group;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupRepository extends JpaRepository<Group, UUID> {
    Optional<Group> findBySlug(String slug);
    boolean existsBySlug(String slug);
    List<Group> findByActiveTrueOrderByNameAsc();
}