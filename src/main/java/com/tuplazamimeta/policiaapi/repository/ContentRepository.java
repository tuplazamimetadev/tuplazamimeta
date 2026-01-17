package com.tuplazamimeta.policiaapi.repository;

import com.tuplazamimeta.policiaapi.model.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List; // <--- Importante

public interface ContentRepository extends JpaRepository<Content, Long> {
    // Definimos el método para ordenar por ID
    List<Content> findAllByOrderByIdAsc();
}