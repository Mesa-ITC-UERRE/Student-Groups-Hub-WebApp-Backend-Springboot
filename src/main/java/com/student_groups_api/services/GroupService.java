package com.student_groups_api.services;

import com.student_groups_api.dto.groups.GroupMemberResponse;
import com.student_groups_api.dto.groups.GroupResponse;
import com.student_groups_api.dto.groups.JoinGroupResponse;
import com.student_groups_api.exceptions.NotFoundException;
import com.student_groups_api.model.Group;
import com.student_groups_api.model.GroupMembership;
import com.student_groups_api.model.User;
import com.student_groups_api.model.enums.MembershipStatus;
import com.student_groups_api.repositories.GroupMembershipRepository;
import com.student_groups_api.repositories.GroupRepository;
import com.student_groups_api.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class GroupService {

    private final GroupRepository groupRepository;
    private final GroupMembershipRepository groupMembershipRepository;
    private final CurrentUserService currentUserService;

    public GroupService(
            GroupRepository groupRepository,
            GroupMembershipRepository groupMembershipRepository,
            CurrentUserService currentUserService
    ) {
        this.groupRepository = groupRepository;
        this.groupMembershipRepository = groupMembershipRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public List<GroupResponse> getAllActiveGroups() {
        return groupRepository.findByActiveTrueOrderByNameAsc()
                .stream()
                .map(this::toGroupResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public GroupResponse getBySlug(String slug) {
        Group group = groupRepository.findBySlug(slug)
                .orElseThrow(() -> new NotFoundException("Group not found for slug: " + slug));

        return toGroupResponse(group);
    }

    @Transactional
    public JoinGroupResponse requestJoin(UUID groupId) {
        User currentUser = currentUserService.getCurrentUser();

        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundException("Group not found: " + groupId));

        GroupMembership membership = groupMembershipRepository
                .findByGroupIdAndUserId(groupId, currentUser.getId())
                .orElse(null);

        if (membership == null) {
            membership = new GroupMembership();
            membership.setGroup(group);
            membership.setUser(currentUser);
            membership.setStatus(MembershipStatus.pending);
            membership.setRequestedAt(Instant.now());
            membership = groupMembershipRepository.save(membership);
        } else {
            switch (membership.getStatus()) {
                case pending, approved -> {
                    // keep current state
                }
                case rejected, left, removed, banned -> {
                    membership.setStatus(MembershipStatus.pending);
                    membership.setRequestedAt(Instant.now());
                    membership.setReviewedAt(null);
                    membership.setReviewedBy(null);
                    membership.setDecisionNotes(null);
                    membership.setJoinedAt(null);
                    membership.setLeftAt(null);
                    membership = groupMembershipRepository.save(membership);
                }
            }
        }

        return new JoinGroupResponse(
                membership.getId(),
                group.getId(),
                currentUser.getId(),
                membership.getStatus().name(),
                membership.getRequestedAt()
        );
    }

    @Transactional(readOnly = true)
    public List<GroupMemberResponse> getApprovedMembers(UUID groupId) {
        if (!groupRepository.existsById(groupId)) {
            throw new NotFoundException("Group not found: " + groupId);
        }

        return groupMembershipRepository
                .findByGroupIdAndStatusOrderByCreatedAtAsc(groupId, MembershipStatus.approved)
                .stream()
                .map(this::toMemberResponse)
                .toList();
    }

    private GroupResponse toGroupResponse(Group group) {
        return new GroupResponse(
                group.getId(),
                group.getSlug(),
                group.getName(),
                group.getDescription(),
                group.getLogoUrl(),
                group.isActive(),
                group.getCreatedAt(),
                group.getUpdatedAt()
        );
    }

    private GroupMemberResponse toMemberResponse(GroupMembership membership) {
        User user = membership.getUser();

        return new GroupMemberResponse(
                membership.getId(),
                user.getId(),
                user.getEmail(),
                user.getDisplayName(),
                user.getAvatarUrl(),
                membership.getStatus().name(),
                membership.getJoinedAt()
        );
    }
}