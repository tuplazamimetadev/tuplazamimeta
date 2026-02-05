package com.tuplazamimeta.policiaapi.controller;

import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
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

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final StripeService stripeService;
    private final UserService userService;

    // Leemos el secreto del Webhook desde application.properties
    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    // --- 1. CREAR SESIÓN DE PAGO (Frontend -> Backend) ---
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

    // --- 2. WEBHOOK (Stripe -> Backend) ---
    @PostMapping("/webhook")
    public ResponseEntity<String> handleStripeWebhook(@RequestBody String payload, @RequestHeader("Stripe-Signature") String sigHeader) {
        Event event;

        try {
            // Verificar que la petición viene realmente de Stripe
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (SignatureVerificationException e) {
            System.out.println("⚠️ Error de firma en el Webhook");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Firma inválida");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error procesando webhook");
        }

        // Si el evento es "pago completado"
        if ("checkout.session.completed".equals(event.getType())) {
            
            // Recuperamos los datos de la sesión
            Session session = (Session) event.getDataObjectDeserializer().getObject().orElse(null);

            if (session != null) {
                // Leemos los metadatos que guardamos en StripeService
                String userId = session.getMetadata().get("userId");
                String planName = session.getMetadata().get("planName");

                System.out.println("✅ Pago recibido de Usuario ID: " + userId + " para el plan: " + planName);

                // Calcular duración (Por defecto 1 mes, si implementas anuales podrías cambiar esto)
                int durationMonths = 1;
                if (planName.toLowerCase().contains("anual")) {
                    durationMonths = 12;
                }

                // ACTIVAR LA SUSCRIPCIÓN
                try {
                    userService.activateSubscription(userId, durationMonths, planName);
                    System.out.println("🚀 Suscripción activada correctamente.");
                } catch (Exception e) {
                    System.err.println("❌ Error activando suscripción: " + e.getMessage());
                    // Aunque falle la activación interna, devolvemos OK a Stripe para que no reintente infinitamente
                    // Lo ideal sería guardar un log de error para revisarlo manualmente.
                }
            }
        }

        return ResponseEntity.ok("Recibido");
    }
}