package com.tuplazamimeta.policiaapi.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "questions")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(columnDefinition = "TEXT")
    private String statement;

    // --- MAPEO EXPLÍCITO DE COLUMNAS ---
    @Column(name = "option_a") // Aseguramos que busque "option_a"
    private String optionA;

    @Column(name = "option_b")
    private String optionB;

    @Column(name = "option_c")
    private String optionC;

    @Column(name = "option_d")
    private String optionD;

    @Column(name = "correct_option", columnDefinition = "CHAR(1)") // CHAR(1) como vimos antes
    private String correctOption;

    @Column(columnDefinition = "TEXT")
    private String explanation;
    
    private String category;
}