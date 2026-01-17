package com.tuplazamimeta.policiaapi.service;

import com.tuplazamimeta.policiaapi.dto.response.ContentResponse;
import com.tuplazamimeta.policiaapi.dto.response.MaterialResponse;
import com.tuplazamimeta.policiaapi.mapper.ContentMapper;
import com.tuplazamimeta.policiaapi.model.Content;
import com.tuplazamimeta.policiaapi.model.MaterialType; // <--- Importante: Importar el Enum
import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.repository.ContentRepository;
import com.tuplazamimeta.policiaapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@SuppressWarnings("null") // Ignorar advertencias de nulidad en inyecciones
public class ContentService {

    private final ContentRepository contentRepository;
    private final ContentMapper contentMapper;
    private final UserRepository userRepository;

    public List<ContentResponse> getAllContents() {
        // 1. Obtener usuario actual
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email).orElse(null);
        String role = (user != null) ? user.getRole() : "PRUEBA";

        // CASO: SUPUESTOS (No ve temario teórico)
        if ("SUPUESTOS".equals(role)) {
            return Collections.emptyList();
        }

        // Obtener todos los contenidos ordenados
        List<Content> allContents = contentRepository.findAllByOrderByIdAsc();

        return allContents.stream()
            // FILTRO 1: ¿Qué temas ve el usuario?
            .filter(content -> {
                if ("PRUEBA".equals(role)) {
                    // El rol PRUEBA solo ve el Tema con ID 1
                    return content.getId() == 1;
                }
                return true; // El resto ven todos los temas (la teoría de A y B)
            })
            .map(content -> {
                ContentResponse response = contentMapper.toResponse(content);
                
                // FILTRO 2: ¿Qué materiales DENTRO del tema puede ver?
                if ("TEST".equals(role)) {
                    // El rol TEST solo ve materiales que sean de tipo TEST
                    List<MaterialResponse> filteredMaterials = response.getMaterials().stream()
                        // CORRECCIÓN: Comparamos Enums correctamente con ==
                        .filter(m -> m.getType() == MaterialType.TEST) 
                        .collect(Collectors.toList());
                    
                    response.setMaterials(filteredMaterials);
                }
                
                // Roles ADMIN, COMPLETO y PRUEBA (en su tema 1) ven todos los tipos de materiales (PDF, VIDEO, etc.)
                return response;
            })
            .collect(Collectors.toList());
    }

    public Content createContent(Content content) {
        // Validación básica para evitar NullPointer
        if (content == null) {
            throw new IllegalArgumentException("El contenido no puede ser nulo");
        }
        return contentRepository.save(content);
    }
}