package com.DropZone.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentIntentResponse {
    private String clientSecret;
    private String paymentIntentId;
    private Long amount;
    private String currency;
}