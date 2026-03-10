package com.student_groups_api.dto.groups;

import java.time.Instant;
import java.util.UUID;

public record GroupRegistrationRequestResponse(
        UUID id,
        UUID requestedByUserId,
        String proposedGroupName,
        String proposedDescription,
        String contactEmail,
        String status,
        Instant createdAt,
        Instant reviewedAt,
        String decisionNotes
) {}