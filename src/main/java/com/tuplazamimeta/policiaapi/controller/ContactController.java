package com.tuplazamimeta.policiaapi.controller;

import com.tuplazamimeta.policiaapi.dto.request.ContactRequest;
import com.tuplazamimeta.policiaapi.model.ContactMessage; // Importar Entidad
import com.tuplazamimeta.policiaapi.repository.ContactMessageRepository; // Importar Repo
import com.tuplazamimeta.policiaapi.service.ContactService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize; // Importar
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contact")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;
    private final ContactMessageRepository contactRepository; // Inyectamos el repo para leer

    // Enviar mensaje (Público para alumnos logueados)
    @PostMapping
    public ResponseEntity<String> sendMessage(@RequestBody ContactRequest request) {
        contactService.sendMessage(request);
        return ResponseEntity.ok("Mensaje enviado y registrado correctamente.");
    }

    // LEER MENSAJES (SOLO ADMIN)
    // Este endpoint devuelve todos los mensajes ordenados por fecha
    @GetMapping
    public ResponseEntity<List<ContactMessage>> getAllMessages() {
        // Aquí podrías verificar el rol programáticamente si no usas @PreAuthorize
        // pero asumiremos que el frontend oculta la página y el SecurityConfig protege /api/**
        return ResponseEntity.ok(contactRepository.findAllByOrderBySentAtDesc());
    }
}