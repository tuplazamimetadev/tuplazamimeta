package com.tuplazamimeta.policiaapi.dto.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class NewsResponse {
    private Long id;
    private String title;
    private String description;
    private String link;
    private LocalDateTime publishedDate;
}