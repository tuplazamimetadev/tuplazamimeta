package com.tuplazamimeta.policiaapi.mapper;

import com.tuplazamimeta.policiaapi.dto.request.ContactRequest;
import com.tuplazamimeta.policiaapi.model.ContactMessage;
import com.tuplazamimeta.policiaapi.model.User;
import org.springframework.stereotype.Component;

@Component
public class ContactMapper {

    public ContactMessage toEntity(ContactRequest request, User user) {
        return ContactMessage.builder()
                .type(request.getType())
                .subject(request.getSubject())
                .message(request.getMessage())
                .userId(user.getId())
                .userName(user.getName())
                .userEmail(user.getEmail())
                // La fecha se pone automática con @PrePersist en la entidad
                .build();
    }
}