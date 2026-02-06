package com.tuplazamimeta.policiaapi.repository;

import com.tuplazamimeta.policiaapi.model.Question;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {
    
    @Query(value = "SELECT * FROM questions ORDER BY RAND()", nativeQuery = true)
    List<Question> findRandomQuestions(Pageable pageable);

    // Consulta para una sola categoría
    @Query(value = "SELECT * FROM questions WHERE category = :category ORDER BY RAND()", nativeQuery = true)
    List<Question> findRandomQuestionsByCategory(@Param("category") String category, Pageable pageable);

    // NUEVA: Consulta para lista de categorías (Bloque A / B)
    @Query(value = "SELECT * FROM questions WHERE category IN :categories ORDER BY RAND()", nativeQuery = true)
    List<Question> findRandomQuestionsByCategories(@Param("categories") List<String> categories, Pageable pageable);
}