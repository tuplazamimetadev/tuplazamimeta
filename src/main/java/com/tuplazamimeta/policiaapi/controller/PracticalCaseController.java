package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.dto.response.PracticalCaseResponse;
import com.tuplazamimeta.policiaapi.mapper.PracticalCaseMapper;
import com.tuplazamimeta.policiaapi.model.PracticalCase;
import com.tuplazamimeta.policiaapi.repository.PracticalCaseRepository;
import com.tuplazamimeta.policiaapi.service.PracticalCaseService; // <--- Importar el servicio
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile; // <--- Importante

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/practical-cases")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class PracticalCaseController {

    private final PracticalCaseRepository repository;
    private final PracticalCaseService practicalCaseService; // <--- Usamos el servicio
    private final PracticalCaseMapper mapper;

    // Obtener todos
    @GetMapping
    public List<PracticalCaseResponse> getAllCases() {
        return repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
    }

    // --- MODIFICADO: Subir archivo (Multipart) ---
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE) // Importante: consume multipart
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')")
    public ResponseEntity<PracticalCaseResponse> createCase(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description
    ) {
        try {
            // Llamamos al servicio para que gestione la subida a Azure
            PracticalCase savedCase = practicalCaseService.uploadCase(file, title, description);
            return ResponseEntity.ok(mapper.toResponse(savedCase));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // --- MODIFICADO: Borrar usando el servicio ---
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        if (!repository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        // Usamos el servicio para borrar también de Azure
        practicalCaseService.deleteCase(id);
        return ResponseEntity.noContent().build();
    }
}