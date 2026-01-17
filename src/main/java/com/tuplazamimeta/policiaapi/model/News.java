package com.tuplazamimeta.policiaapi.model;

import jakarta.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "app_news")
public class News {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String link;

    @Column(name = "published_date")
    private LocalDateTime publishedDate;

    @PrePersist
    protected void onCreate() {
        this.publishedDate = LocalDateTime.now();
    }
}