package com.tuplazamimeta.policiaapi.repository;

import com.tuplazamimeta.policiaapi.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface NotificationRepository extends JpaRepository<Notification, Long> {
    // Buscar las de un usuario ordenadas por fecha (las nuevas primero)
    List<Notification> findByUserIdOrderByCreatedAtDesc(Long userId);
}