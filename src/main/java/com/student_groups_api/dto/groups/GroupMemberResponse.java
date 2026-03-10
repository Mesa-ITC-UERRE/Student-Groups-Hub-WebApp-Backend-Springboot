package com.student_groups_api.dto.groups;

import java.time.Instant;
import java.util.UUID;

public record GroupMemberResponse(
        UUID membershipId,
        UUID userId,
        String email,
        String displayName,
        String avatarUrl,
        String status,
        Instant joinedAt
) {
}