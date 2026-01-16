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
import org.springframework.transaction.annotation.Transactional; // <--- IMPORTANTE: DE SPRING
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

    public Material uploadFile(MultipartFile file, String title, String typeString, Long contentId) throws IOException {
        if (contentId == null) throw new IllegalArgumentException("El ID del tema es obligatorio");
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Tema no encontrado"));
        
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
        if (contentId == null) throw new IllegalArgumentException("El ID del tema es obligatorio");
        Content content = contentRepository.findById(contentId).orElseThrow(() -> new RuntimeException("Tema no encontrado"));
        
        Material material = new Material();
        material.setTitle(title);
        material.setType(MaterialType.valueOf(typeString));
        material.setUrl(url);
        material.setContent(content);
        return materialRepository.save(material);
    }

    // --- AQUÍ ESTÁ LA MAGIA PARA ARREGLAR EL BORRADO ---
    @Transactional // Asegura que la operación sea atómica
    public void deleteMaterial(Long id) {
        System.out.println(">>> INICIANDO BORRADO DE MATERIAL ID: " + id); // LOG

        if (id == null) throw new IllegalArgumentException("El ID no puede ser nulo");

        Material material = materialRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Material no encontrado"));

        // 1. Intentar borrar de Azure (si es un archivo)
        if (material.getUrl() != null && material.getUrl().contains(containerName) && material.getUrl().contains("blob.core.windows.net")) {
            try {
                String fileName = material.getUrl().substring(material.getUrl().lastIndexOf("/") + 1);
                fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
                
                System.out.println(">>> INTENTANDO BORRAR BLOB AZURE: " + fileName); // LOG

                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
                BlobClient blobClient = containerClient.getBlobClient(fileName);
                
                if (blobClient.exists()) {
                    blobClient.delete();
                    System.out.println(">>> BLOB BORRADO CORRECTAMENTE"); // LOG
                } else {
                    System.out.println(">>> EL BLOB NO EXISTÍA, CONTINUAMOS..."); // LOG
                }
            } catch (Exception e) {
                // Si falla Azure, NO paramos. Queremos borrar el registro de la BD sí o sí.
                System.err.println(">>> ERROR EN AZURE (IGNORADO PARA LIMPIAR BD): " + e.getMessage());
            }
        }

        // 2. Borrar de la base de datos
        System.out.println(">>> BORRANDO REGISTRO DE BASE DE DATOS..."); // LOG
        materialRepository.delete(material);
        
        // 3. FORZAR el borrado inmediato (Esto evita que se quede 'colgado' y falle después)
        materialRepository.flush(); 
        
        System.out.println(">>> ¡BORRADO COMPLETO Y CONFIRMADO!"); // LOG
    }
}