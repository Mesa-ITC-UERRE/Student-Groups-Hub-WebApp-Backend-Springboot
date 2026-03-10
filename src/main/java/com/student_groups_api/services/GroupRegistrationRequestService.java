package com.student_groups_api.services;

import com.student_groups_api.dto.groups.CreateGroupRegistrationRequest;
import com.student_groups_api.dto.groups.GroupRegistrationRequestResponse;
import com.student_groups_api.exceptions.NotFoundException;
import com.student_groups_api.model.GroupRegistrationRequest;
import com.student_groups_api.model.User;
import com.student_groups_api.model.enums.RequestStatus;
import com.student_groups_api.repositories.GroupRegistrationRequestRepository;
import com.student_groups_api.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class GroupRegistrationRequestService {

    private final GroupRegistrationRequestRepository repository;
    private final CurrentUserService currentUserService;

    public GroupRegistrationRequestService(
            GroupRegistrationRequestRepository repository,
            CurrentUserService currentUserService
    ) {
        this.repository = repository;
        this.currentUserService = currentUserService;
    }

    @Transactional
    public GroupRegistrationRequestResponse create(CreateGroupRegistrationRequest request) {

        User currentUser = currentUserService.getCurrentUser();

        GroupRegistrationRequest entity = new GroupRegistrationRequest();
        entity.setRequestedBy(currentUser);
        entity.setProposedGroupName(request.proposedGroupName());
        entity.setProposedDescription(request.proposedDescription());
        entity.setContactEmail(request.contactEmail());
        entity.setStatus(RequestStatus.pending);

        entity = repository.save(entity);

        return toResponse(entity);
    }

    @Transactional(readOnly = true)
    public List<GroupRegistrationRequestResponse> getMyRequests() {

        User currentUser = currentUserService.getCurrentUser();

        return repository.findByRequestedByIdOrderByCreatedAtDesc(currentUser.getId())
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GroupRegistrationRequestResponse getById(UUID id) {

        GroupRegistrationRequest request = repository.findById(id)
                .orElseThrow(() -> new NotFoundException("Group registration request not found: " + id));

        return toResponse(request);
    }

    private GroupRegistrationRequestResponse toResponse(GroupRegistrationRequest entity) {

        return new GroupRegistrationRequestResponse(
                entity.getId(),
                entity.getRequestedBy().getId(),
                entity.getProposedGroupName(),
                entity.getProposedDescription(),
                entity.getContactEmail(),
                entity.getStatus().name(),
                entity.getCreatedAt(),
                entity.getReviewedAt(),
                entity.getDecisionNotes()
        );
    }
}