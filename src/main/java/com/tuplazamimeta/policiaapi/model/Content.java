package com.tuplazamimeta.policiaapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity
@Table(name = "app_contents")
public class Content {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String description;
    
    @Column(name = "is_premium")
    private Boolean isPremium;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    // Relación: Un tema tiene muchos materiales
    @OneToMany(mappedBy = "content", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Material> materials;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
    }
}