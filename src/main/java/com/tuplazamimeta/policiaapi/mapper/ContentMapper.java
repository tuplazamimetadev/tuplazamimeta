package com.tuplazamimeta.policiaapi.mapper;

import com.tuplazamimeta.policiaapi.dto.response.ContentResponse;
import com.tuplazamimeta.policiaapi.dto.response.MaterialResponse;
import com.tuplazamimeta.policiaapi.model.Content;
import com.tuplazamimeta.policiaapi.model.Material;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ContentMapper {

    private MaterialResponse mapMaterial(Material material) {
        MaterialResponse dto = new MaterialResponse();
        dto.setId(material.getId());
        dto.setTitle(material.getTitle());
        dto.setType(material.getType());
        dto.setUrl(material.getUrl());
        return dto;
    }

    public ContentResponse toResponse(Content content) {
        if (content == null) return null;
        
        ContentResponse dto = new ContentResponse();
        dto.setId(content.getId());
        dto.setTitle(content.getTitle());
        dto.setDescription(content.getDescription());
        dto.setIsPremium(content.getIsPremium());

        // Mapeamos los hijos
        if (content.getMaterials() != null) {
            dto.setMaterials(
                content.getMaterials().stream()
                .map(this::mapMaterial)
                .collect(Collectors.toList())
            );
        }
        return dto;
    }

    public List<ContentResponse> toResponseList(List<Content> contents) {
        if (contents == null) return List.of();
        return contents.stream().map(this::toResponse).collect(Collectors.toList());
    }
}