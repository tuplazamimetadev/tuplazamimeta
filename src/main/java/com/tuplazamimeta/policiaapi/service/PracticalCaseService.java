package com.tuplazamimeta.policiaapi.service;

import com.azure.storage.blob.BlobClient;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.tuplazamimeta.policiaapi.model.PracticalCase;
import com.tuplazamimeta.policiaapi.repository.PracticalCaseRepository;
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
public class PracticalCaseService {

    private final PracticalCaseRepository repository;
    private final BlobServiceClient blobServiceClient;

    @Value("${azure.storage.container-name}")
    private String containerName;

    // --- SUBIR ARCHIVO A AZURE Y GUARDAR EN BD ---
    public PracticalCase uploadCase(MultipartFile file, String title, String description) throws IOException {
        
        // 1. Generar nombre único para el archivo
        String filename = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
        
        // 2. Subir a Azure Blob Storage
        BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
        BlobClient blobClient = containerClient.getBlobClient(filename);
        blobClient.upload(file.getInputStream(), file.getSize(), true);
        
        // 3. Crear y guardar la entidad
        PracticalCase practicalCase = new PracticalCase();
        practicalCase.setTitle(title);
        practicalCase.setDescription(description);
        practicalCase.setUrl(blobClient.getBlobUrl()); // Guardamos la URL de Azure
        practicalCase.setPublic(false); // Por defecto privado (o pásalo como parámetro si quieres)
        
        return repository.save(practicalCase);
    }

    // --- BORRAR ARCHIVO DE AZURE Y DE BD ---
    @Transactional
    public void deleteCase(Long id) {
        PracticalCase practicalCase = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supuesto no encontrado"));

        // 1. Borrar archivo físico de Azure
        if (practicalCase.getUrl() != null && practicalCase.getUrl().contains(containerName)) {
            try {
                // Extraer el nombre del archivo de la URL
                String fileName = practicalCase.getUrl().substring(practicalCase.getUrl().lastIndexOf("/") + 1);
                fileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
                
                BlobContainerClient containerClient = blobServiceClient.getBlobContainerClient(containerName);
                BlobClient blobClient = containerClient.getBlobClient(fileName);
                
                if (blobClient.exists()) {
                    blobClient.delete();
                }
            } catch (Exception e) {
                System.err.println("Error al borrar de Azure (no bloqueante): " + e.getMessage());
            }
        }

        // 2. Borrar registro de la base de datos
        repository.delete(practicalCase);
    }
}