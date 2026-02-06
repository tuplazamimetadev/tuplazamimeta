package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.model.Question;
import com.tuplazamimeta.policiaapi.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

    private final QuestionRepository questionRepository;

    @GetMapping("/random")
    public ResponseEntity<List<Question>> generateRandomTest(
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(required = false) String category
    ) {
        // Creamos la paginación para limitar el número de preguntas (size)
        Pageable limit = PageRequest.of(0, size);
        List<Question> exam;

        if (category != null && !category.isEmpty() && !category.equals("GENERAL")) {
            // Generador específico (BLOQUE_A, BLOQUE_B...)
            exam = questionRepository.findRandomQuestionsByCategory(category, limit);
        } else {
            // Generador general (Cualquier categoría)
            exam = questionRepository.findRandomQuestions(limit);
        }
        
        return ResponseEntity.ok(exam);
    }
}