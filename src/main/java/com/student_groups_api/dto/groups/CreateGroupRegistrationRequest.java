package com.student_groups_api.dto.groups;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateGroupRegistrationRequest(

        @NotBlank
        String proposedGroupName,

        String proposedDescription,

        @Email
        @NotBlank
        String contactEmail

) {}