package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.repository.UserRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/subscription")
@RequiredArgsConstructor
public class SubscriptionController {

    private final UserRepository userRepository;

    @PostMapping("/upgrade")
    public ResponseEntity<?> upgradePlan(@RequestBody SubscriptionRequest request) {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        // Lógica de planes
        switch (request.getPlan()) {
            case "Solo Test":
                user.setRole("TEST");
                break;
            case "Solo Supuestos":
                user.setRole("SUPUESTOS");
                break;
            case "Opositor Completo":
                user.setRole("COMPLETO"); // O 'PREMIUM' según uses en front
                break;
            default:
                return ResponseEntity.badRequest().body("Plan desconocido");
        }
        
        // Sumar 30 días de suscripción
        user.setExpirationDate(LocalDate.now().plusDays(30));
        userRepository.save(user);

        return ResponseEntity.ok("Plan actualizado correctamente");
    }

    @Data
    static class SubscriptionRequest {
        private String plan;
    }
}