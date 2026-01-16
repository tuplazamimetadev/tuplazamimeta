package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.model.Material;
import com.tuplazamimeta.policiaapi.service.MaterialService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/materials")
@RequiredArgsConstructor
public class MaterialController {

    private final MaterialService materialService;

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')")
    public ResponseEntity<Material> uploadMaterial(
            @RequestParam("file") MultipartFile file,
            @RequestParam("title") String title,
            @RequestParam("type") String type,
            @RequestParam("topicId") Long topicId,
            @RequestParam(value = "description", required = false) String description // <--- NUEVO
    ) {
        try {
            // Pasamos la descripción al servicio
            return ResponseEntity.ok(materialService.uploadFile(file, title, type, topicId, description));
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    // 2. CREAR LINKS -> CAMBIADO A hasAnyAuthority
    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')") // <--- CAMBIO AQUÍ
    public ResponseEntity<Material> createLink(@RequestBody LinkRequest request) {
        Material newMaterial = materialService.createLink(
                request.url(),
                request.title(),
                request.type(),
                request.topicId());
        return ResponseEntity.ok(newMaterial);
    }

    // 3. BORRAR -> CAMBIADO A hasAnyAuthority
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'PROFESOR')") // <--- CAMBIO AQUÍ
    public ResponseEntity<Void> deleteMaterial(@PathVariable Long id) {
        materialService.deleteMaterial(id);
        return ResponseEntity.noContent().build();
    }
}

// DTO Auxiliar
record LinkRequest(String title, String type, String url, Long topicId) {
}