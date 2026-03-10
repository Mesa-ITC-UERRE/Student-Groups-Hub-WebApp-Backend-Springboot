package com.student_groups_api.services;

import com.student_groups_api.dto.users.UserResponse;
import com.student_groups_api.exceptions.NotFoundException;
import com.student_groups_api.model.User;
import com.student_groups_api.repositories.UserRepository;
import com.student_groups_api.security.CurrentUserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CurrentUserService currentUserService;

    public UserService(UserRepository userRepository, CurrentUserService currentUserService) {
        this.userRepository = userRepository;
        this.currentUserService = currentUserService;
    }

    @Transactional(readOnly = true)
    public UserResponse getById(UUID id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found: " + id));
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse getByEntraOid(String entraOid) {
        User user = userRepository.findByEntraOid(entraOid)
                .orElseThrow(() -> new NotFoundException("User not found for entraOid: " + entraOid));
        return toResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse me() {

        /*
         * ============================================================
         * TEMP TEST MODE
         * ============================================================
         * Authentication is temporarily disabled for backend testing.
         * Because there is no JWT in the SecurityContext, we return a
         * fixed seeded test user instead of resolving the current user.
         *
         * REQUIRED TEST DATA:
         * A user with entra_oid = 'test-user-1' must exist in the DB.
         *
         * REMOVE THIS AFTER TESTING:
         * Restore the original implementation:
         *
         *     User currentUser = currentUserService.getCurrentUser();
         *     return toResponse(currentUser);
         *
         * ============================================================
         */

        User testUser = userRepository.findByEntraOid("test-user-1")
                .orElseThrow(() -> new NotFoundException(
                        "Test fallback user not found. Insert a user with entra_oid='test-user-1'"
                ));

        return toResponse(testUser);
    }

    private static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getEntraOid(),
                user.getEmail(),
                user.getDisplayName(),
                user.getAvatarUrl(),
                user.isPlatformAdmin(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}