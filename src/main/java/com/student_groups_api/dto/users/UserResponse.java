package com.student_groups_api.dto.users;

import java.time.Instant;
import java.util.UUID;

public record UserResponse(
        UUID id,
        String entraOid,
        String email,
        String displayName,
        String avatarUrl,
        boolean isPlatformAdmin,
        Instant createdAt,
        Instant updatedAt
) {
}