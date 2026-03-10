package com.student_groups_api.dto.groups;

import java.time.Instant;
import java.util.UUID;

public record GroupResponse(
        UUID id,
        String slug,
        String name,
        String description,
        String logoUrl,
        boolean active,
        Instant createdAt,
        Instant updatedAt
) {
}