package com.student_groups_api.repositories;

import com.student_groups_api.model.GroupRegistrationRequest;
import com.student_groups_api.model.enums.RequestStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface GroupRegistrationRequestRepository extends JpaRepository<GroupRegistrationRequest, UUID> {

    List<GroupRegistrationRequest> findByRequestedByIdOrderByCreatedAtDesc(UUID userId);

    List<GroupRegistrationRequest> findByStatusOrderByCreatedAtAsc(RequestStatus status);
}