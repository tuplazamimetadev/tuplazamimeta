package com.tuplazamimeta.policiaapi.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.tuplazamimeta.policiaapi.model.Content;
import com.tuplazamimeta.policiaapi.model.Material;
import com.tuplazamimeta.policiaapi.model.MaterialType;
import com.tuplazamimeta.policiaapi.repository.ContentRepository;
import com.tuplazamimeta.policiaapi.repository.MaterialRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder; // Necesario para decodificar nombres con espacios
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

    // ... (Tus métodos uploadFile y createLink SE QUEDAN IGUAL, no los borres) ...
    // Copia los métodos uploadFile y createLink del paso anterior aquí.
    // Solo añado el NUEVO método de borrar abajo:

    public Material uploadFile(MultipartFile file, String title, String typeString, Long contentId) throws IOException {
        if (contentId == null)
            throw new IllegalArgumentException("El ID del tema es obligatorio");
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(filename);
        blobClient.upload(file.getInputStream(), file.getSize(), true);

        Material material = new Material();
        material.setTitle(title);
        material.setType(MaterialType.valueOf(typeString));
        material.setUrl(blobClient.getBlobUrl());
        material.setContent(content);
        return materialRepository.save(material);
    }

    public Material createLink(String url, String title, String typeString, Long contentId) {
        if (contentId == null)
            throw new IllegalArgumentException("El ID del tema es obligatorio");
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado"));

        Material material = new Material();
        material.setTitle(title);
        material.setType(MaterialType.valueOf(typeString));
        material.setUrl(url);
        material.setContent(content);
        return materialRepository.save(material);
    }

    @Transactional
    public void deleteMaterial(Long id) {
        // 1. Comprobación de seguridad: Si el ID es nulo, paramos aquí.
        if (id == null) {
            throw new IllegalArgumentException("El ID del material no puede ser nulo");
        }

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        // Si es un archivo de Azure, borrarlo de la nube primero
        if (material.getUrl() != null && material.getUrl().contains(containerName)
                && material.getUrl().contains("blob.core.windows.net")) {
            try {
                // Extraer el nombre del archivo de la URL
                String fileName = material.getUrl().substring(material.getUrl().lastIndexOf("/") + 1);
                // Decodificar por si tiene espacios (%20)
                fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);

                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
                BlobClient blobClient = containerClient.getBlobClient(fileName);

                if (blobClient.exists()) {
                    blobClient.delete();
                }
            } catch (Exception e) {
                System.err.println("Error borrando archivo de Azure: " + e.getMessage());
                // Continuamos para borrarlo de la BD aunque falle Azure
            }
        }

        // Borrar de la base de datos
        materialRepository.delete(material);
    }
}