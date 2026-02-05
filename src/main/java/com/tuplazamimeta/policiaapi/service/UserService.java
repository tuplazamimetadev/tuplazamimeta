package com.tuplazamimeta.policiaapi.service;

import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // Este método se llamará automáticamente cuando Stripe confirme el pago
    public void activateSubscription(String userId, int months, String planName) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + userId));

        // --- 1. ASIGNACIÓN DE ROLES SEGÚN EL PLAN ---
        if (planName != null) {
            String planUpper = planName.toUpperCase(); // Convertimos a mayúsculas para evitar errores

            if (planUpper.contains("COMPLETO")) {
                user.setRole("COMPLETO");
            } else if (planUpper.contains("SUPUESTOS")) {
                user.setRole("SUPUESTOS");
            } else if (planUpper.contains("TEST")) {
                user.setRole("TEST");
            } else {
                // Si viene un nombre raro, por defecto asignamos TEST o lo dejamos como estaba
                // user.setRole("TEST"); 
            }
        }

        // --- 2. CÁLCULO DE LA FECHA DE VENCIMIENTO ---
        LocalDate startDate = LocalDate.now();

        // Si el usuario ya tenía una suscripción activa que vence en el futuro,
        // sumamos el tiempo a esa fecha (para no robarle días).
        if (user.getExpirationDate() != null && user.getExpirationDate().isAfter(startDate)) {
            startDate = user.getExpirationDate();
        }

        user.setExpirationDate(startDate.plusMonths(months));

        // Guardamos los cambios en la base de datos
        userRepository.save(user);
    }

    // Métodos auxiliares para buscar usuarios (usados por otros controladores)
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con ID: " + id));
    }
    
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con email: " + email));
    }

    public void saveStripeInfo(String userId, String customerId, String subscriptionId) {
        User user = userRepository.findById(Long.parseLong(userId))
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        
        user.setStripeCustomerId(customerId);
        user.setStripeSubscriptionId(subscriptionId);
        userRepository.save(user);
    }
}