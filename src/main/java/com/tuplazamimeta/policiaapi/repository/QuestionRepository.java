package com.tuplazamimeta.policiaapi.repository;

import com.tuplazamimeta.policiaapi.model.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    // Consulta nativa para obtener preguntas aleatorias
    // El Pageable se encarga del "LIMIT" automáticamente
    @Query(value = "SELECT * FROM questions ORDER BY RAND()", nativeQuery = true)
    List<Question> findRandomQuestions(Pageable pageable);

    // Consulta filtrada por categoría y aleatoria
    @Query(value = "SELECT * FROM questions WHERE category = :category ORDER BY RAND()", nativeQuery = true)
    List<Question> findRandomQuestionsByCategory(@Param("category") String category, Pageable pageable);
}