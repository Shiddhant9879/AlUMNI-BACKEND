package com.alumni.alumni_backend.controller;

import org.springframework.web.bind.annotation.*;

import com.alumni.alumni_backend.dto.LoginRequestDto;
import com.alumni.alumni_backend.dto.LoginResponseDto;
import com.alumni.alumni_backend.model.User;
import com.alumni.alumni_backend.security.JwtUtil;
import com.alumni.alumni_backend.service.UserService;
import com.alumni.alumni_backend.dto.ForgotPasswordRequestDto;
import com.alumni.alumni_backend.dto.ResetPasswordRequestDto;

@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class UserController {

    private final UserService userService;
    private final JwtUtil jwtUtil;

    public UserController(UserService userService, JwtUtil jwtUtil) {
        this.userService = userService;
        this.jwtUtil = jwtUtil;
    }

    // 🧑‍🎓 / 🧑‍💼 REGISTER (optional for you; admin already exists)
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        return userService.registerUser(user);
    }

    // 🔐 LOGIN → JWT ISSUED
    @PostMapping("/login")
    public LoginResponseDto login(@RequestBody LoginRequestDto dto) {

        User user = userService.loginUser(
                dto.getEmail(),
                dto.getPassword());

        // 🔑 JWT contains email + role
        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole());

        return new LoginResponseDto(token, user);
    }

    // 🔍 GET USER BY ID (protected via security config)
    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUserById(id);
    }

    @PostMapping("/forgot-password/email")
    public String forgotPassword(@RequestBody ForgotPasswordRequestDto request) {
        userService.sendResetPasswordEmail(request.getEmail());
        return "Reset link generated successfully";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestBody ResetPasswordRequestDto request) {
        userService.resetPassword(request.getToken(), request.getNewPassword());
        return "Password reset successful";
    }
}
