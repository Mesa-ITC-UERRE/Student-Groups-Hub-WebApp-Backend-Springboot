package com.student_groups_api.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

@Component
public class JwtUserExtractor {

    private static final String REQUIRED_DOMAIN = "@uerre.mx";

    public JwtUserPrincipal extractCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null) {
            throw new IllegalStateException("No authentication found in security context");
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof Jwt jwt)) {
            throw new IllegalStateException("No authenticated JWT user found in security context");
        }

        String entraOid = firstNonBlank(
                jwt.getClaimAsString("oid"),
                jwt.getSubject()
        );

        String email = firstNonBlank(
                jwt.getClaimAsString("preferred_username"),
                jwt.getClaimAsString("email"),
                jwt.getClaimAsString("upn")
        );

        String displayName = firstNonBlank(
                jwt.getClaimAsString("name"),
                jwt.getClaimAsString("preferred_username"),
                email
        );

        if (entraOid == null || entraOid.isBlank()) {
            throw new IllegalStateException("JWT does not contain a valid 'oid' claim");
        }

        if (email == null || email.isBlank()) {
            throw new IllegalStateException("JWT does not contain a valid email claim");
        }

        String normalizedEmail = email.trim().toLowerCase();

        if (!normalizedEmail.endsWith(REQUIRED_DOMAIN)) {
            throw new IllegalStateException("Only @uerre.mx accounts are allowed");
        }

        return new JwtUserPrincipal(
                entraOid.trim(),
                normalizedEmail,
                displayName == null ? null : displayName.trim()
        );
    }

    private static String firstNonBlank(String... values) {
        if (values == null) {
            return null;
        }

        for (String value : values) {
            if (value != null && !value.isBlank()) {
                return value;
            }
        }

        return null;
    }
}