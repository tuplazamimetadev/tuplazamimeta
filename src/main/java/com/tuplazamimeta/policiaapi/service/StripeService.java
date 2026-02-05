package com.tuplazamimeta.policiaapi.service;

import com.stripe.Stripe;
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

    public Session createCheckoutSession(PaymentRequest paymentRequest, User user) throws Exception {
        // Configuramos los parámetros de la sesión de pago
        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT) // O SUBSCRIPTION si configuras productos recurrentes en Stripe
                .setSuccessUrl(clientUrl + "/profile?payment=success")
                .setCancelUrl(clientUrl + "/planes?payment=cancelled")
                .setCustomerEmail(user.getEmail()) // Pre-rellena el email del usuario
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("eur")
                                                .setUnitAmount(paymentRequest.getAmount()) // Precio en céntimos
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Suscripción " + paymentRequest.getPlanName())
                                                                .build())
                                                .build())
                                .build())
                // Metadatos para saber quién pagó cuando Stripe nos avise
                .putMetadata("userId", user.getId().toString())
                .putMetadata("planName", paymentRequest.getPlanName())
                .build();

        return Session.create(params);
    }
}