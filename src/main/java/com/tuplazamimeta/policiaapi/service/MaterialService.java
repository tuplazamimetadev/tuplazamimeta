package com.tuplazamimeta.policiaapi.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.tuplazamimeta.policiaapi.model.Content;
import com.tuplazamimeta.policiaapi.model.Material;
import com.tuplazamimeta.policiaapi.model.MaterialType;
import com.tuplazamimeta.policiaapi.repository.ContentRepository;
import com.tuplazamimeta.policiaapi.repository.MaterialRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final ContentRepository contentRepository;
    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.container-name}")
    private String containerName;

    public Material uploadFile(MultipartFile file, String title, String typeString, Long contentId, String description) throws IOException {
        if (contentId == null) throw new IllegalArgumentException("El ID del tema es obligatorio");
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Tema no encontrado"));
        
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(filename);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        
        Material material = new Material();
        material.setTitle(title);
        material.setDescription(description);
        material.setType(MaterialType.valueOf(typeString)); 
        material.setUrl(blobClient.getBlobUrl());
        material.setContent(content);
        return materialRepository.save(material);
    }

    public Material createLink(String url, String title, String typeString, Long contentId) {
        if (contentId == null) throw new IllegalArgumentException("El ID del tema es obligatorio");
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Tema no encontrado"));
        
        Material material = new Material();
        material.setTitle(title);
        material.setType(MaterialType.valueOf(typeString));
        material.setUrl(url);
        material.setContent(content);
        return materialRepository.save(material);
    }

    @Transactional
    public void deleteMaterial(Long id) {
        if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo");

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        // 1. Borrar de Azure
        if (material.getUrl() != null && material.getUrl().contains(containerName)) {
            try {
                // CORRECCIÓN 1: Usamos las clases importadas en vez de escribir la ruta entera
                String fileName = material.getUrl().substring(material.getUrl().lastIndexOf("/") + 1);
                fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
                
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
                BlobClient blobClient = containerClient.getBlobClient(fileName);
                
                if (blobClient.exists()) {
                    blobClient.delete();
                }
            } catch (Exception e) {
                System.err.println("Error Azure (no crítico): " + e.getMessage());
            }
        }

        // 2. CORRECCIÓN 2 (Null Safety): Asignamos a variable para asegurar que no es nulo
        Content parentContent = material.getContent();
        if (parentContent != null) {
            parentContent.getMaterials().remove(material);
            contentRepository.save(parentContent);
        }

        // 3. Borrar de la BD y forzar
        materialRepository.delete(material);
        materialRepository.flush();
    }
}