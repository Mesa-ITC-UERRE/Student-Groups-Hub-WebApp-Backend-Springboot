package com.student_groups_api.dto.groups;

import java.time.Instant;
import java.util.UUID;

public record JoinGroupResponse(
        UUID membershipId,
        UUID groupId,
        UUID userId,
        String status,
        Instant requestedAt
) {
}