package com.tuplazamimeta.policiaapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "contact_messages")
public class ContactMessage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long userId; // ID del alumno
    private String userName;
    private String userEmail;

    private String type; // "GENERAL" o "TUTORIA"
    private String subject; // Asunto o Tema
    
    @Column(columnDefinition = "TEXT")
    private String message; // Descripción

    private LocalDateTime sentAt;
    
    @PrePersist
    protected void onCreate() {
        sentAt = LocalDateTime.now();
    }
}