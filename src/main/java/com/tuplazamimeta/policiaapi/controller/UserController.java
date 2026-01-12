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
        // 1. Spring Security ya sabe el email gracias al Token
        String email = authentication.getName();

        // 2. Buscamos al usuario en la BD
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 3. Traducimos el ROL interno a un nombre bonito para la web
        String displayRole = "Alumno en Pruebas";
        String dbRole = user.getRole() != null ? user.getRole() : "STUDENT";
        
        if ("ADMIN".equals(dbRole)) {
            displayRole = "Administrador";
        } else if ("PREMIUM".equals(dbRole) || "FULL".equals(dbRole)) {
            displayRole = "Opositor Completo"; // Título dorado
        } else if ("TEST".equals(dbRole)) {
            displayRole = "Solo Test";
        }

        // 4. Calculamos fecha expiración (Ej: 1 año desde registro)
        String expiration = "Indefinido";
        if (user.getCreatedAt() != null) {
            expiration = user.getCreatedAt().plusYears(1).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        }

        return ResponseEntity.ok(new UserProfileResponse(
                user.getName(),
                user.getEmail(),
                displayRole,
                expiration
        ));
    }
}