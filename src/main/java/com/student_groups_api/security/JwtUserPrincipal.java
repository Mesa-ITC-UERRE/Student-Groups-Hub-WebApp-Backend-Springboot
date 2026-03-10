package com.student_groups_api.security;

public record JwtUserPrincipal(
        String entraOid,
        String email,
        String displayName
) {
}