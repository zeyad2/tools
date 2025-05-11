package org.os.minisocial.user.service;

import org.os.minisocial.shared.dto.ProfileDTO;
import org.os.minisocial.user.entity.User;
import org.os.minisocial.user.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.PasswordHash;
import java.util.Optional;

@Stateless
public class UserService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordHash passwordHasher;

    public Optional<User> updateProfile(String email, ProfileDTO profileDTO) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) return Optional.empty();

        User user = userOpt.get();
        user.setName(profileDTO.getName());
        user.setBio(profileDTO.getBio());

        if (profileDTO.getEmail() != null && !profileDTO.getEmail().isEmpty()) {
            user.setEmail(profileDTO.getEmail());
        }

        if (profileDTO.getNewPassword() != null && !profileDTO.getNewPassword().isEmpty()) {
            user.setPassword(passwordHasher.generate(profileDTO.getNewPassword().toCharArray()));
        }

        userRepository.update(user);
        return Optional.of(user);
    }

    public Optional<User> getUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}