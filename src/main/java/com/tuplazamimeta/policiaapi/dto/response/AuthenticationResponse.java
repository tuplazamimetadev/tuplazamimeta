package com.tuplazamimeta.policiaapi.dto.response;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private String name; // Devolvemos el nombre para ponerlo en el Navbar
}