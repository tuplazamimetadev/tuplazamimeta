package com.tuplazamimeta.policiaapi.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PracticalCaseResponse {
    private Long id;
    private String title;
    private String description;
    private String url;
    private boolean isPublic;
    private LocalDateTime createdAt;
}