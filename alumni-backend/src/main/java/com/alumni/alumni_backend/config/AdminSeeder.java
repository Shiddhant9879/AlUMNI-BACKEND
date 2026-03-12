package com.alumni.alumni_backend.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.alumni.alumni_backend.model.User;
import com.alumni.alumni_backend.repository.UserRepository;

@Configuration
public class AdminSeeder implements CommandLineRunner {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AdminSeeder(UserRepository userRepository,
            PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {

        String adminEmail = "admin@college.edu";

        boolean adminExists = userRepository.existsByEmail(adminEmail);

        if (!adminExists) {
            User admin = new User();
            admin.setName("System Admin");
            admin.setEmail(adminEmail);
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setRole("ADMIN");

            userRepository.save(admin);

            System.out.println("✅ ADMIN user created: " + adminEmail);
        } else {
            System.out.println("ℹ️ ADMIN user already exists. Skipping seeding.");
        }
    }
}
