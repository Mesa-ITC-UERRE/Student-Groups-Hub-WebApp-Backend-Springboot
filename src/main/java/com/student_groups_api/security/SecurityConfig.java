package com.student_groups_api.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/*
 * ============================================================================
 * TEMPORARY SECURITY CONFIG FOR BACKEND TESTING
 * ============================================================================
 *
 * PURPOSE
 * -------
 * This file temporarily disables authentication so backend endpoints can be
 * tested before Microsoft Entra ID login is fully integrated from the frontend.
 *
 * CURRENT BEHAVIOR
 * ----------------
 * - All requests are allowed.
 * - No JWT token is required.
 * - No Entra validation happens.
 * - Useful for testing:
 *      - user creation
 *      - group creation
 *      - group membership flow
 *      - request/response DTOs
 *      - repository/service/controller behavior
 *
 * IMPORTANT
 * ---------
 * Endpoints that depend on the authenticated current user will NOT behave as
 * they should in final production mode, because there is no JWT being read.
 *
 * In particular, anything using:
 *      - CurrentUserService
 *      - JwtUserExtractor
 *      - /api/users/me
 *
 * should be considered temporary / incomplete until Entra is restored.
 *
 * ============================================================================
 * TESTING STEPS FOR NOW
 * ============================================================================
 *
 * 1) Keep this temporary file active.
 *
 * 2) Start the backend:
 *      ./mvnw spring-boot:run
 *
 * 3) Test endpoints directly from Postman / Thunder Client / curl.
 *
 * 4) Focus on backend logic that does NOT require real login yet:
 *      - Create test users
 *      - Create groups
 *      - Create group registration requests
 *      - Create memberships
 *      - Read groups, members, requests, etc.
 *
 * 5) Once frontend login with Entra is ready:
 *      - restore the original SecurityConfig
 *      - re-enable JWT resource server
 *      - re-test /api/users/me with Bearer token
 *
 * ============================================================================
 * WHAT TO RESTORE LATER
 * ============================================================================
 *
 * When Entra is ready, restore:
 *
 *      .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
 *
 *      .authorizeHttpRequests(auth -> auth
 *              .requestMatchers("/actuator/**").permitAll()
 *              .requestMatchers("/api/public/**").permitAll()
 *              .anyRequest().authenticated()
 *      )
 *
 *      .oauth2ResourceServer(oauth -> oauth
 *              .jwt(Customizer.withDefaults())
 *      )
 *
 * ============================================================================
 * SEARCH MARKER
 * ============================================================================
 *
 * Search for: AUTH_TEMP
 *
 * This makes it easy to find and remove this temporary config later.
 * ============================================================================
 */
@Configuration
@EnableMethodSecurity
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                /*
                 * AUTH_TEMP:
                 * Disable CSRF for API testing simplicity.
                 * This is normal for APIs, especially when using Postman/curl.
                 */
                .csrf(csrf -> csrf.disable())

                /*
                 * AUTH_TEMP:
                 * Permit every request temporarily so the backend can be tested
                 * without JWT tokens or Entra login.
                 */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/actuator/**").permitAll()
                        .anyRequest().permitAll()
                );

        return http.build();
    }
}