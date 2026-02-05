package com.tuplazamimeta.policiaapi.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.exception.EventDataObjectDeserializationException; // IMPORTANTE: Nueva importación
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.StripeObject;
import com.stripe.model.Subscription;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.SubscriptionUpdateParams;
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
    public ResponseEntity<Map<String, String>> createCheckoutSession(@RequestBody PaymentRequest request,
            Authentication authentication) {
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
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {
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
                // Si la versión no coincide, forzamos la lectura (SOLUCIÓN AL ERROR DE
                // COMPILACIÓN)
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
                String rawPlanName = session.getMetadata().get("planName");

                // --- NUEVO: Capturamos los IDs de la suscripción ---
                String customerId = session.getCustomer();
                String subscriptionId = session.getSubscription();

                System.out.println("✅ NUEVA SUSCRIPCIÓN -> User: " + userId + " | SubID: " + subscriptionId);

                try {
                    // 1. Guardamos los IDs de Stripe en el usuario
                    userService.saveStripeInfo(userId, customerId, subscriptionId);

                    // 2. Activamos el rol (Mapeando el nombre correctamente)
                    String internalRole = mapToInternalRole(rawPlanName);
                    userService.activateSubscription(userId, 1, internalRole);

                    System.out.println("🚀 ¡SUSCRIPCIÓN ACTIVADA Y VINCULADA!");
                } catch (Exception e) {
                    System.err.println("❌ Error guardando suscripción: " + e.getMessage());
                    e.printStackTrace();
                }
            }
        }

        return ResponseEntity.ok("Recibido");
    }
    @PostMapping("/cancel-subscription")
    public ResponseEntity<String> cancelSubscription(Authentication auth) {
        try {
            User user = (User) auth.getPrincipal();
            String subId = user.getStripeSubscriptionId();

            if (subId == null || subId.isEmpty()) {
                return ResponseEntity.badRequest().body("No tienes ninguna suscripción activa para cancelar.");
            }

            // Llamamos a Stripe
            Subscription subscription = Subscription.retrieve(subId);
            
            // Le decimos que cancele AL FINAL del periodo (para que disfrute lo que ya pagó)
            SubscriptionUpdateParams params = SubscriptionUpdateParams.builder()
                    .setCancelAtPeriodEnd(true)
                    .build();
            
            subscription.update(params);

            return ResponseEntity.ok("Suscripción cancelada correctamente. Tu acceso continuará hasta el final del ciclo de facturación.");

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error al cancelar la suscripción: " + e.getMessage());
        }
    }

    // --- Método auxiliar para limpiar los nombres ---
    private String mapToInternalRole(String rawName) {
        if (rawName == null)
            return "GRATIS";

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