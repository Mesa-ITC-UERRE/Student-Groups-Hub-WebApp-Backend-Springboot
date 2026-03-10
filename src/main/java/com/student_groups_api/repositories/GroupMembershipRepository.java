package com.student_groups_api.repositories;

import com.student_groups_api.model.GroupMembership;
import com.student_groups_api.model.enums.MembershipStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface GroupMembershipRepository extends JpaRepository<GroupMembership, UUID> {

    Optional<GroupMembership> findByGroupIdAndUserId(UUID groupId, UUID userId);

    List<GroupMembership> findByGroupIdAndStatusOrderByCreatedAtAsc(UUID groupId, MembershipStatus status);

    List<GroupMembership> findByUserIdAndStatusOrderByCreatedAtDesc(UUID userId, MembershipStatus status);

    boolean existsByGroupIdAndUserId(UUID groupId, UUID userId);
}