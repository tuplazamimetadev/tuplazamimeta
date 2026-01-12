package com.tuplazamimeta.policiaapi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserProfileResponse {
    private String name;
    private String email;
    private String role;
    private String expiration;
}