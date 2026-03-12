package com.alumni.alumni_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.alumni.alumni_backend.model.User;

import java.util.Optional;
import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);

    List<User> findByRole(String role);

    Optional<User> findByResetToken(String token);
}
