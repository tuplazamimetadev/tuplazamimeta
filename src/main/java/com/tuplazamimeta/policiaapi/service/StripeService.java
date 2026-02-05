package com.tuplazamimeta.policiaapi.service;

import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import com.tuplazamimeta.policiaapi.dto.request.PaymentRequest;
import com.tuplazamimeta.policiaapi.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import jakarta.annotation.PostConstruct;

@Service
public class StripeService {

    @Value("${stripe.api.key}")
    private String secretKey;

    @Value("${client.url}")
    private String clientUrl;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

public Session createCheckoutSession(PaymentRequest request, User user) throws StripeException {
        
        SessionCreateParams params = SessionCreateParams.builder()
                // 1. MODO SUSCRIPCIÓN (Esto activa el cobro mensual automático)
                .setMode(SessionCreateParams.Mode.SUBSCRIPTION) 
                
                .setSuccessUrl(clientUrl + "/profile?payment=success")
                .setCancelUrl(clientUrl + "/suscripcion?payment=cancelled")
                .setCustomerEmail(user.getEmail()) 
                
                // 2. Metadatos para el Webhook
                .putMetadata("userId", user.getId().toString())
                .putMetadata("planName", request.getPlanName())
                
                // 3. El producto específico a cobrar
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPrice(request.getPriceId()) // <-- Usamos el ID de precio (price_...)
                                .build())
                .build();

        return Session.create(params);
    }
}