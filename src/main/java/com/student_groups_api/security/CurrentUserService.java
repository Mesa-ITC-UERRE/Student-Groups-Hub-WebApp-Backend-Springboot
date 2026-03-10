package com.student_groups_api.security;

import com.student_groups_api.model.User;
import com.student_groups_api.repositories.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CurrentUserService {

    private final JwtUserExtractor jwtUserExtractor;
    private final UserRepository userRepository;

    public CurrentUserService(JwtUserExtractor jwtUserExtractor, UserRepository userRepository) {
        this.jwtUserExtractor = jwtUserExtractor;
        this.userRepository = userRepository;
    }

    @Transactional
    public User getCurrentUser() {
        JwtUserPrincipal principal = jwtUserExtractor.extractCurrentUser();

        User user = userRepository.findByEntraOid(principal.entraOid())
                .orElseGet(() -> createUser(principal));

        boolean changed = false;

        if (!principal.email().equalsIgnoreCase(user.getEmail())) {
            user.setEmail(principal.email());
            changed = true;
        }

        if (principal.displayName() != null && !principal.displayName().equals(user.getDisplayName())) {
            user.setDisplayName(principal.displayName());
            changed = true;
        }

        if (changed) {
            user = userRepository.save(user);
        }

        return user;
    }

    private User createUser(JwtUserPrincipal principal) {
        User user = new User();
        user.setEntraOid(principal.entraOid());
        user.setEmail(principal.email());
        user.setDisplayName(principal.displayName());
        user.setPlatformAdmin(false);
        return userRepository.save(user);
    }
}