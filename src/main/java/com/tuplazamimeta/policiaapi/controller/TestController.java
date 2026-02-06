package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.model.Question;
import com.tuplazamimeta.policiaapi.repository.QuestionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/tests")
@RequiredArgsConstructor
public class TestController {

    private final QuestionRepository questionRepository;

// ... imports

    @GetMapping("/random")
    public ResponseEntity<List<Question>> generateRandomTest(
            @RequestParam(defaultValue = "50") int size,
            @RequestParam(required = false) String category
    ) {
        Pageable limit = PageRequest.of(0, size);
        List<Question> exam;

        if (category == null || category.isEmpty() || category.equals("GENERAL")) {
            exam = questionRepository.findRandomQuestions(limit);
        } 
        else if (category.equals("BLOQUE_A")) {
            // CORREGIDO: Bloque A ahora incluye FUNCIÓN PÚBLICA (TREBEP)
            List<String> categoriesA = Arrays.asList(
                "CONSTITUCIONAL", 
                "ADMINISTRATIVO", 
                "FUNCION_PUBLICA", // <--- CAMBIO AQUÍ
                "SEGURIDAD_PUBLICA"
            );
            exam = questionRepository.findRandomQuestionsByCategories(categoriesA, limit);
        } 
        else if (category.equals("BLOQUE_B")) {
            List<String> categoriesB = Arrays.asList(
                "PENAL", 
                "NORMATIVA_AUTONOMICA", 
                "TRAFICO"
            );
            exam = questionRepository.findRandomQuestionsByCategories(categoriesB, limit);
        } 
        else {
            exam = questionRepository.findRandomQuestionsByCategory(category, limit);
        }
        
        return ResponseEntity.ok(exam);
    }
}