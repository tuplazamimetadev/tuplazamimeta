package com.tuplazamimeta.policiaapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "app_materials")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Enumerated(EnumType.STRING) // Guarda "VIDEO", "TEST" como texto en BBDD
    @Column(name = "material_type")
    private MaterialType type;

    private String url;

    @ManyToOne
    @JoinColumn(name = "content_id")
    private Content content;

    @Column(length = 500) 
    private String description;
}