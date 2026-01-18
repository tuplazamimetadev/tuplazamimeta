package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.dto.response.UserProfileResponse;
import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.repository.UserRepository;

import lombok.Data;
import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;
    @Autowired
    PasswordEncoder passwordEncoder;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String role = user.getRole() != null ? user.getRole() : "STUDENT";

        // CORRECCIÓN: Usar la fecha real de expiración
        String expiration = "Indefinido";
        if (user.getExpirationDate() != null) {
            expiration = user.getExpirationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else if (user.getCreatedAt() != null) {
            // Fallback por si acaso es null (aunque el script V12 lo arregla)
            expiration = user.getCreatedAt().plusDays(30).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        return ResponseEntity.ok(new UserProfileResponse(
                user.getName(),
                user.getEmail(),
                role,
                expiration));
    }

    @PutMapping("/me")
    public ResponseEntity<User> updateProfile(@RequestBody UpdateProfileDto request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        if (request.getName() != null && !request.getName().isBlank()) {
            user.setName(request.getName());
        }
        if (request.getPassword() != null && !request.getPassword().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.getPassword()));
        }

        return ResponseEntity.ok(userRepository.save(user));
    }

    @Data
    static class UpdateProfileDto {
        private String name;
        private String password;
    }
}