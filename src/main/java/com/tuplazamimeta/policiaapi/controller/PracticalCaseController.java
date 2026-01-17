package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.dto.response.PracticalCaseResponse;
import com.tuplazamimeta.policiaapi.mapper.PracticalCaseMapper;
import com.tuplazamimeta.policiaapi.model.PracticalCase;
import com.tuplazamimeta.policiaapi.repository.PracticalCaseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/practical-cases")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class PracticalCaseController {

    private final PracticalCaseRepository repository;
    private final PracticalCaseMapper mapper;

    // Obtener todos los supuestos (convertidos a DTO)
    @GetMapping
    public List<PracticalCaseResponse> getAllCases() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // Subir un supuesto (Admin/Profesor)
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')")
    public ResponseEntity<PracticalCaseResponse> createCase(@RequestBody PracticalCase practicalCase) {
        if (practicalCase == null) {
            return ResponseEntity.badRequest().build();
        }
        PracticalCase savedCase = repository.save(practicalCase);
        return ResponseEntity.ok(mapper.toResponse(savedCase));
    }

    // Borrar un supuesto
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}