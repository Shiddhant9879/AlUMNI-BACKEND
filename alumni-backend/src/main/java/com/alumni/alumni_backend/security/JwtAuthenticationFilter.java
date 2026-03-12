package com.alumni.alumni_backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.alumni.alumni_backend.model.User;
import com.alumni.alumni_backend.service.UserService;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    // ✅ PUBLIC ROUTES (no JWT required)
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String path = request.getServletPath();

        return path.equals("/api/users/login")
                || path.equals("/api/users/register")
                || path.startsWith("/api/users/forgot-password")
                || path.equals("/api/users/reset-password")
                || path.startsWith("/api/ai/"); // 🔥 allow all AI endpoints
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {

            String token = authHeader.substring(7);

            if (jwtUtil.validateToken(token)) {

                // Extract + normalize email
                String email = jwtUtil.extractEmail(token);
                email = email.trim().toLowerCase();

                try {
                    User user = userService.getUserByEmail(email);

                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                            email,
                            null,
                            List.of(new SimpleGrantedAuthority("ROLE_" + user.getRole())));

                    SecurityContextHolder.getContext().setAuthentication(authentication);

                } catch (RuntimeException ex) {
                    System.out.println("User not found for email from token: " + email);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}