package com.tuplazamimeta.policiaapi.dto.response;

import com.tuplazamimeta.policiaapi.model.MaterialType;
import lombok.Data;

@Data
public class MaterialResponse {
    private Long id;
    private String title;
    private MaterialType type; 
    private String url;
}