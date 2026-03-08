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

    @Transactional
    public UserResponse me() {
        User currentUser = currentUserService.getCurrentUser();
        return toResponse(currentUser);
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