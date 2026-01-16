package com.tuplazamimeta.policiaapi.config;

import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AzureConfig {

    // Leemos la cadena de conexión de tu application.properties
    @Value("${azure.storage.connection-string}")
    private String connectionString;

    @Bean
    public BlobServiceClient blobServiceClient() {
        // Creamos y devolvemos el cliente listo para usarse
        return new BlobServiceClientBuilder()
                .connectionString(connectionString)
                .buildClient();
    }
}