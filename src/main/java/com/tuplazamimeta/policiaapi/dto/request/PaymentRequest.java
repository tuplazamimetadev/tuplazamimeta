package com.tuplazamimeta.policiaapi.dto.request;

import lombok.Data;

@Data
public class PaymentRequest {
    private String planName; 
    private String priceId;  
}