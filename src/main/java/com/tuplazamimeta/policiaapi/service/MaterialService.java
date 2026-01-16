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
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MaterialService {

    private final MaterialRepository materialRepository;
    private final ContentRepository contentRepository;
    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.container-name}")
    private String containerName;

    // OPCIÓN A: Subir Fichero (PDF/WORD)
    public Material uploadFile(MultipartFile file, String title, String typeString, Long contentId) throws IOException {
        // 1. Validación de seguridad (quita el warning)
        if (contentId == null) {
            throw new IllegalArgumentException("El ID del tema es obligatorio");
        }

        // 2. Buscar el Tema
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado con id: " + contentId));

        // 3. Generar nombre único
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

        // 4. Subir a Azure
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(filename);
        
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        String fileUrl = blobClient.getBlobUrl();

        // 5. Guardar en Base de Datos
        Material material = new Material();
        material.setTitle(title);
        material.setType(MaterialType.valueOf(typeString)); 
        material.setUrl(fileUrl);
        material.setContent(content);

        return materialRepository.save(material);
    }

    // OPCIÓN B: Crear Link (VIDEO/LINK)
    public Material createLink(String url, String title, String typeString, Long contentId) {
        // 1. Validación de seguridad (quita el warning)
        if (contentId == null) {
            throw new IllegalArgumentException("El ID del tema es obligatorio");
        }

        // 2. Buscar el Tema
        Content content = contentRepository.findById(contentId)
                .orElseThrow(() -> new RuntimeException("Tema no encontrado con id: " + contentId));

        // 3. Guardar
        Material material = new Material();
        material.setTitle(title);
        material.setType(MaterialType.valueOf(typeString));
        material.setUrl(url);
        material.setContent(content);

        return materialRepository.save(material);
    }
}