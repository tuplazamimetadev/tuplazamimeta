package com.tuplazamimeta.policiaapi.service;

import com.tuplazamimeta.policiaapi.dto.request.ContactRequest;
import com.tuplazamimeta.policiaapi.mapper.ContactMapper;
import com.tuplazamimeta.policiaapi.model.ContactMessage;
import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.repository.ContactMessageRepository;
import com.tuplazamimeta.policiaapi.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ContactService {

    private final ContactMessageRepository contactRepository;
    private final UserRepository userRepository;
    private final ContactMapper contactMapper;

    public void sendMessage(ContactRequest request) {
        // 1. Obtener el usuario autenticado del contexto de seguridad
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        // 2. Convertir DTO a Entidad usando el Mapper
        ContactMessage messageEntity = contactMapper.toEntity(request, user);

        // 3. Guardar en Base de Datos
        contactRepository.save(messageEntity);
        
        // Aquí podrías añadir lógica para enviar un email real al administrador
        // emailService.sendNotification(messageEntity);
    }
    public void deleteMessage(Long id) {
        if (!contactRepository.existsById(id)) {
            throw new RuntimeException("El mensaje no existe");
        }
        contactRepository.deleteById(id);
    }
}