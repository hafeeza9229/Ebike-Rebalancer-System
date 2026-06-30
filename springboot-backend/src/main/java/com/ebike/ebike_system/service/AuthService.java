package com.ebike.ebike_system.service;

import com.ebike.ebike_system.model.User;
import com.ebike.ebike_system.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository,
                       PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // REGISTER
    public String register(User user) {

        boolean exists = userRepository.findByUsername(user.getUsername()).isPresent();

        if (exists) {
            throw new RuntimeException("Username already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole("USER");

        userRepository.save(user);

        return "User registered successfully";
    }

    // LOGIN
    public Map<String, Object> login(User request) {

        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return Map.of(
                "message", "Login successful",
                "username", user.getUsername(),
                "role", user.getRole(),
                "name", user.getName() != null ? user.getName() : user.getUsername().split("@")[0],
                "loggedIn", true
        );
    }
}
