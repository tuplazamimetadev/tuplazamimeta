package com.tuplazamimeta.policiaapi.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.EventDataObjectDeserializationException; // IMPORTANTE: Nueva importación
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.tuplazamimeta.policiaapi.dto.request.PaymentRequest;
import com.tuplazamimeta.policiaapi.model.User;
import com.tuplazamimeta.policiaapi.service.StripeService;
import com.tuplazamimeta.policiaapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
// Eliminada la importación de Optional que no se usaba

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final StripeService stripeService;
    private final UserService userService;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody PaymentRequest request, Authentication authentication) {
        try {
            User user = (User) authentication.getPrincipal();
            Session session = stripeService.createCheckoutSession(request, user);
            Map<String, String> response = new HashMap<>();
            response.put("url", session.getUrl());
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("⚠️ Firma inválida del Webhook");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firma inválida");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error procesando webhook");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            
            EventDataObjectDeserializer dataObjectDeserializer = event.getDataObjectDeserializer();
            Session session = null;

            // Intentamos obtener el objeto de forma segura
            if (dataObjectDeserializer.getObject().isPresent()) {
                session = (Session) dataObjectDeserializer.getObject().get();
            } else {
                // Si la versión no coincide, forzamos la lectura (SOLUCIÓN AL ERROR DE COMPILACIÓN)
                System.out.println("⚠️ Versión API diferente. Intentando deserialización forzada...");
                try {
                    StripeObject stripeObject = dataObjectDeserializer.deserializeUnsafe();
                    if (stripeObject instanceof Session) {
                        session = (Session) stripeObject;
                    }
                } catch (EventDataObjectDeserializationException e) {
                    System.err.println("❌ Error al forzar deserialización: " + e.getMessage());
                    return ResponseEntity.ok("Error interno de versión ignorado");
                }
            }

            if (session != null) {
                String userId = session.getMetadata().get("userId");
                String rawPlanName = session.getMetadata().get("planName"); // Ej: "Solo Supuestos"

                // TRADUCCIÓN DE NOMBRES (SOLUCIÓN AL PROBLEMA DE LÓGICA)
                String internalRole = mapToInternalRole(rawPlanName);

                System.out.println("✅ PROCESANDO: Usuario " + userId + " | Plan Original: " + rawPlanName + " -> Rol Interno: " + internalRole);

                int durationMonths = 1;
                if (rawPlanName != null && rawPlanName.toLowerCase().contains("anual")) {
                    durationMonths = 12;
                }

                try {
                    // Usamos internalRole en lugar de rawPlanName
                    userService.activateSubscription(userId, durationMonths, internalRole);
                    System.out.println("🚀 ¡SUSCRIPCIÓN ACTIVADA CON ÉXITO!");
                } catch (Exception e) {
                    System.err.println("❌ Error en base de datos: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return ResponseEntity.ok("Recibido");
    }

    // --- Método auxiliar para limpiar los nombres ---
    private String mapToInternalRole(String rawName) {
        if (rawName == null) return "GRATIS";
        
        String lowerName = rawName.toLowerCase();
        
        if (lowerName.contains("test")) {
            return "TEST";
        } else if (lowerName.contains("supuestos") || lowerName.contains("practical")) {
            return "SUPUESTOS";
        } else if (lowerName.contains("completo") || lowerName.contains("premium")) {
            return "COMPLETO"; 
        }
        
        return "PRUEBA"; // Por defecto si no reconoce nada
    }
}