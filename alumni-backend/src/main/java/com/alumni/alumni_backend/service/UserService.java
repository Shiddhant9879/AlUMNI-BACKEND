package com.alumni.alumni_backend.service;

import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alumni.alumni_backend.model.User;
import com.alumni.alumni_backend.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final EmailService emailService; // 🔐 added

    public UserService(UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            EmailService emailService) { // 🔐 updated constructor
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    // =========================
    // REGISTER USER
    // =========================
    public User registerUser(User user) {

        String email = user.getEmail().trim().toLowerCase();
        user.setEmail(email);

        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setCreatedAt(LocalDateTime.now());

        return userRepository.save(user);
    }

    // =========================
    // LOGIN USER (FIXED)
    // =========================
    public User loginUser(String email, String password) {

        email = email.trim().toLowerCase();
        password = password.trim();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Invalid email or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        return user;
    }

    // =========================
    // SEND RESET PASSWORD EMAIL
    // =========================
    public void sendResetPasswordEmail(String email) {

        email = email.trim().toLowerCase();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = java.util.UUID.randomUUID().toString();

        user.setResetToken(token);
        user.setResetTokenExpiry(LocalDateTime.now().plusMinutes(15));

        userRepository.save(user);

        String resetLink = "http://localhost:5173/reset-password?token=" + token;

        // 🔐 send via email instead of console
        emailService.sendResetEmail(user.getEmail(), resetLink);
    }

    // =========================
    // FETCH USER
    // =========================
    public User getUserById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email.trim().toLowerCase())
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    // =========================
    // RESET PASSWORD
    // =========================
    public void resetPassword(String token, String newPassword) {

        User user = userRepository.findByResetToken(token)
                .orElseThrow(() -> new RuntimeException("Invalid reset token"));

        if (user.getResetTokenExpiry().isBefore(LocalDateTime.now())) {
            throw new RuntimeException("Reset token has expired");
        }

        user.setPassword(passwordEncoder.encode(newPassword));

        user.setResetToken(null);
        user.setResetTokenExpiry(null);

        userRepository.save(user);
    }
}
