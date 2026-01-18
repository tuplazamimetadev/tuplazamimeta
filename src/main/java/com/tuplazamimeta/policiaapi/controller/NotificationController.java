package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.model.Notification;
import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.repository.NotificationRepository;
import com.tuplazamimeta.policiaapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
@RequiredArgsConstructor
public class NotificationController {

    private final NotificationRepository notificationRepository;
    private final UserRepository userRepository;

    @GetMapping
    public ResponseEntity<List<Notification>> getMyNotifications() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(notificationRepository.findByUserIdOrderByCreatedAtDesc(user.getId()));
    }

    @PutMapping("/{id}/read")
    public ResponseEntity<Void> markAsRead(@PathVariable Long id) {
        notificationRepository.findById(id).ifPresent(n -> {
            n.setRead(true);
            notificationRepository.save(n);
        });
        return ResponseEntity.ok().build();
    }
    
    // Endpoint para que el Admin cree notificaciones (Opcional)
    @PostMapping
    public ResponseEntity<Notification> create(@RequestBody Notification n) {
        return ResponseEntity.ok(notificationRepository.save(n));
    }
}