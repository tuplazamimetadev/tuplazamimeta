package com.tuplazamimeta.policiaapi.dto.response;

import lombok.Data;
import java.util.List;

@Data
public class ContentResponse {
    private Long id;
    private String title;
    private String description;
    private Boolean isPremium;
    private List<MaterialResponse> materials; 
}