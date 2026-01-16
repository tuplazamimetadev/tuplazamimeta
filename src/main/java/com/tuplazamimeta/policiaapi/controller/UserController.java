package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.dto.response.UserProfileResponse;
import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserRepository userRepository;

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getCurrentUser(Authentication authentication) {
        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        String role = user.getRole() != null ? user.getRole() : "STUDENT";

        // Fecha de expiración
        String expiration = "Indefinido";
        if (user.getCreatedAt() != null) {
            expiration = user.getCreatedAt().plusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        return ResponseEntity.ok(new UserProfileResponse(
                user.getName(),
                user.getEmail(),
                role, // <--- Enviamos "ADMIN" puro
                expiration
        ));
    }
}