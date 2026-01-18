package com.tuplazamimeta.policiaapi.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ContactRequest {
    private String type;    // "GENERAL" o "TUTORIA"
    private String subject; // Asunto
    private String message; // Mensaje
}