package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.dto.response.PracticalCaseResponse;
import com.tuplazamimeta.policiaapi.mapper.PracticalCaseMapper;
import com.tuplazamimeta.policiaapi.model.PracticalCase;
import com.tuplazamimeta.policiaapi.repository.PracticalCaseRepository;
import com.tuplazamimeta.policiaapi.service.PracticalCaseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/practical-cases")
@RequiredArgsConstructor
@SuppressWarnings("null")
public class PracticalCaseController {

    private final PracticalCaseRepository repository;
    private final PracticalCaseService practicalCaseService;
    private final PracticalCaseMapper mapper;

    // Obtener todos (Con seguridad manual por rol)
    @GetMapping
    public ResponseEntity<List<PracticalCaseResponse>> getAllCases() {
        // Verificar rol
        var auth = SecurityContextHolder.getContext().getAuthentication();
        boolean canView = auth.getAuthorities().stream().anyMatch(a -> 
            a.getAuthority().equals("ADMIN") || 
            a.getAuthority().equals("PROFESOR") || 
            a.getAuthority().equals("SUPUESTOS") || 
            a.getAuthority().equals("COMPLETO")
        );

        if (!canView) {
            // Si es TEST o PRUEBA, prohibido
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        List<PracticalCaseResponse> list = repository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(list);
    }

    // Subir archivo
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')")
    public ResponseEntity<PracticalCaseResponse> createCase(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam(value = "description", required = false) String description
    ) {
        try {
            PracticalCase savedCase = practicalCaseService.uploadCase(file, title, description);
            return ResponseEntity.ok(mapper.toResponse(savedCase));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // Borrar
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')")
    public ResponseEntity<Void> deleteCase(@PathVariable Long id) {
        if (!repository.existsById(id)) return ResponseEntity.notFound().build();
        practicalCaseService.deleteCase(id);
        return ResponseEntity.noContent().build();
    }
}