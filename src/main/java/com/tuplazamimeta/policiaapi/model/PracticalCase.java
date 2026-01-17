package com.tuplazamimeta.policiaapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "app_practical_cases")
public class PracticalCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String url; // Enlace al PDF o Vídeo del supuesto

    @Column(name = "is_public")
    private boolean isPublic; // Por si quieres dejar alguno gratis de muestra

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }
}