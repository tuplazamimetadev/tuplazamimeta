package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.model.Material;
import com.tuplazamimeta.policiaapi.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // <--- IMPORTANTE
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    // 1. SUBIR ARCHIVOS (PDF/WORD) -> Solo ADMIN o PROFESOR
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')") // <--- SEGURIDAD
    public ResponseEntity<Material> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("topicId") Long topicId // El frontend manda "topicId", pero es el ID del Content
    ) {
        try {
            // Pasamos topicId como contentId
            Material newMaterial = materialService.uploadFile(file, title, type, topicId);
            return ResponseEntity.ok(newMaterial);
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 2. CREAR LINKS (VIDEO/LINK) -> Solo ADMIN o PROFESOR
    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESOR')") // <--- SEGURIDAD
    public ResponseEntity<Material> createLink(@RequestBody LinkRequest request) {
        Material newMaterial = materialService.createLink(
                request.url(), 
                request.title(), 
                request.type(), 
                request.topicId()
        );
        return ResponseEntity.ok(newMaterial);
    }
}

// DTO Auxiliar (puede ir en su propio archivo o aquí mismo al final)
record LinkRequest(String title, String type, String url, Long topicId) {}