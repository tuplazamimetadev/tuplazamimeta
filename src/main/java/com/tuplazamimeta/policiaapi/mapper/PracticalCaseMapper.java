package com.tuplazamimeta.policiaapi.mapper;

import com.tuplazamimeta.policiaapi.dto.response.PracticalCaseResponse;
import com.tuplazamimeta.policiaapi.model.PracticalCase;
import org.springframework.stereotype.Component;

@Component
public class PracticalCaseMapper {

    public PracticalCaseResponse toResponse(PracticalCase practicalCase) {
        if (practicalCase == null) return null;

        PracticalCaseResponse response = new PracticalCaseResponse();
        response.setId(practicalCase.getId());
        response.setTitle(practicalCase.getTitle());
        response.setDescription(practicalCase.getDescription());
        response.setUrl(practicalCase.getUrl());
        response.setPublic(practicalCase.isPublic());
        response.setCreatedAt(practicalCase.getCreatedAt());
        
        return response;
    }
}