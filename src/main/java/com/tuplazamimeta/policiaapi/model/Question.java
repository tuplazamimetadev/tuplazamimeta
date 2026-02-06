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

    private String optionA;
    private String optionB;
    private String optionC;
    private String optionD;
    
    @Column(name = "correct_option", columnDefinition = "CHAR(1)")
    private String correctOption; 

    @Column(columnDefinition = "TEXT")
    private String explanation;
    
    private String category;
}