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

        // --- 1. LÓGICA DE CADUCIDAD AUTOMÁTICA ---
        // Si tiene fecha de vencimiento, no es Admin, y la fecha ya pasó (es anterior a
        // hoy)
        if (user.getExpirationDate() != null
                && user.getExpirationDate().isBefore(java.time.LocalDate.now())
                && !"ADMIN".equals(user.getRole())
                && !"PROFESOR".equals(user.getRole())
                && !"STUDENT".equals(user.getRole())) {

            // Lo bajamos al plan de PRUEBA
            user.setRole("PRUEBA");
            user.setExpirationDate(null);

            userRepository.save(user); // Guardamos el cambio en BBDD
        }

        String role = user.getRole() != null ? user.getRole() : "STUDENT";

        // --- 2. MOSTRAR LA FECHA CORRECTAMENTE ---
        String expiration = "Indefinido";

        if ("ADMIN".equals(role) || "PROFESOR".equals(role)) {
            expiration = "Ilimitado";
        } else if (user.getExpirationDate() != null) {
            expiration = user.getExpirationDate().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        } else if (user.getCreatedAt() != null) {
            // Fallback visual solo si no hay fecha explícita
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