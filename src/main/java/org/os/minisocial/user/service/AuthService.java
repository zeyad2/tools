package org.os.minisocial.user.service;

import org.os.minisocial.shared.dto.SignInRequest;
import org.os.minisocial.shared.dto.SignupRequest;
import org.os.minisocial.user.entity.User;
import org.os.minisocial.user.repository.UserRepository;
import jakarta.ejb.Stateless;
import jakarta.inject.Inject;
import jakarta.security.enterprise.identitystore.PasswordHash;
import java.util.Optional;

@Stateless
public class AuthService {
    @Inject
    private UserRepository userRepository;

    @Inject
    private PasswordHash passwordHasher;

    public Optional<User> register(SignupRequest signupRequest) {
        if (userRepository.findByEmail(signupRequest.getEmail()).isPresent()) {
            return Optional.empty();
        }

        User user = new User();
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordHasher.generate(signupRequest.getPassword().toCharArray()));
        user.setName(signupRequest.getName());
        user.setRole(User.Role.valueOf(signupRequest.getRole().toUpperCase()));

        userRepository.save(user);
        return Optional.of(user);
    }

    public Optional<User> authenticate(SignInRequest signInRequest) {
        Optional<User> userOpt = userRepository.findByEmail(signInRequest.getEmail());
        if (userOpt.isEmpty()) return Optional.empty();

        User user = userOpt.get();
        if (passwordHasher.verify(signInRequest.getPassword().toCharArray(), user.getPassword())) {
            return Optional.of(user);
        }
        return Optional.empty();
    }
}