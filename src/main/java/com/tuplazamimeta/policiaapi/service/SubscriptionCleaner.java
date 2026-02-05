package com.tuplazamimeta.policiaapi.service;

import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SubscriptionCleaner {

    private final UserRepository userRepository;

    // Se ejecuta todos los días a las 3:00 AM
    @Scheduled(cron = "0 0 3 * * ?")
    @Transactional
    public void checkExpiredSubscriptions() {
        System.out.println("🧹 Iniciando limpieza de suscripciones caducadas...");

        LocalDate today = LocalDate.now();
        
        List<User> users = userRepository.findAll();
        int count = 0;

        for (User user : users) {
            // Si tiene un rol de pago (no es ADMIN ni GRATIS) y la fecha ya pasó
            if (user.getExpirationDate() != null 
                && user.getExpirationDate().isBefore(today) 
                && !user.getRole().equals("ADMIN") 
                && !user.getRole().equals("PRUEBA")) {
                
                System.out.println("❌ Caducado usuario: " + user.getEmail() + ". Pasando a GRATIS.");
                
                user.setRole("PRUEBA");
                user.setExpirationDate(null);
                userRepository.save(user);
                count++;
            }
        }
        
        System.out.println("✅ Limpieza completada. Usuarios degradados a GRATIS: " + count);
    }
}