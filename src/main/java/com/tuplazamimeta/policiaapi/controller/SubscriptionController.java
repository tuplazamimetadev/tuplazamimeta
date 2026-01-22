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
        // AQUÍ IRÁ LA LÓGICA DE STRIPE / REDSYS EN EL FUTURO
        
        /* CÓDIGO COMENTADO PARA NO REGALAR EL ROL
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        switch (request.getPlan()) {
            case "Solo Test": user.setRole("TEST"); break;
            case "Solo Supuestos": user.setRole("SUPUESTOS"); break;
            case "Opositor Completo": user.setRole("COMPLETO"); break;
            default: return ResponseEntity.badRequest().body("Plan desconocido");
        }
        
        user.setExpirationDate(LocalDate.now().plusDays(30));
        userRepository.save(user);
        */

        // Devolvemos OK pero con un mensaje de aviso (o un error 400 si prefieres que falle)
        return ResponseEntity.ok("La pasarela de pago estará disponible próximamente.");
    }

    @Data
    static class SubscriptionRequest {
        private String plan;
    }
}