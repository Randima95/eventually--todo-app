package edu.jhu.eventually.service;

import edu.jhu.eventually.dto.RegistrationRequest;
import edu.jhu.eventually.model.AppUser;
import edu.jhu.eventually.repository.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AppUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(
            AppUserRepository userRepository,
            PasswordEncoder passwordEncoder
    ) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public void register(RegistrationRequest request) {
        String username = request.getUsername().trim();

        if (userRepository.existsByUsernameIgnoreCase(username)) {
            throw new IllegalArgumentException("That username is already taken.");
        }

        AppUser user = new AppUser(
                username,
                passwordEncoder.encode(request.getPassword())
        );

        userRepository.save(user);
    }

    public AppUser getByUsername(String username) {
        return userRepository.findByUsernameIgnoreCase(username)
                .orElseThrow(() -> new IllegalArgumentException("User was not found."));
    }
}