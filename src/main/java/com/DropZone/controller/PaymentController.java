package com.DropZone.controller;

import com.DropZone.dto.request.PaymentRequest;
import com.DropZone.dto.response.PaymentIntentResponse;
import com.DropZone.entity.Order;
import com.DropZone.entity.Payment;
import com.DropZone.exception.ResourceNotFoundException;
import com.DropZone.repository.OrderRepository;
import com.DropZone.repository.PaymentRepository;
import com.DropZone.service.StripeService;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.model.Event;
import com.stripe.net.Webhook;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PaymentController {

    private final StripeService stripeService;
    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;

    @Value("${stripe.webhook.secret}")
    private String webhookSecret;

    @PostMapping("/create-payment-intent")
    public ResponseEntity<PaymentIntentResponse> createPaymentIntent(
            @Valid @RequestBody PaymentRequest request) throws StripeException {

        Order order = orderRepository.findById(request.getOrderId())
                .orElseThrow(() -> new ResourceNotFoundException("Order not found"));

        PaymentIntent paymentIntent = stripeService.createPaymentIntent(
                order.getTotalPrice(),
                request.getCurrency()
        );

        Payment payment = Payment.builder()
                .stripePaymentId(paymentIntent.getId())
                .amount(order.getTotalPrice())
                .currency(request.getCurrency())
                .order(order)
                .build();

        paymentRepository.save(payment);

        return ResponseEntity.ok(PaymentIntentResponse.builder()
                .clientSecret(paymentIntent.getClientSecret())
                .paymentIntentId(paymentIntent.getId())
                .amount(paymentIntent.getAmount())
                .currency(paymentIntent.getCurrency())
                .build());
    }

    @PostMapping("/webhook")
    public ResponseEntity<String> handleWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;
        try {
            event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Invalid signature");
        }

        if ("payment_intent.succeeded".equals(event.getType())) {
            PaymentIntent paymentIntent = (PaymentIntent) event.getDataObjectDeserializer()
                    .getObject()
                    .orElse(null);

            if (paymentIntent != null) {
                paymentRepository.findByStripePaymentId(paymentIntent.getId())
                        .ifPresent(payment -> {
                            payment.getOrder().setStatus(com.DropZone.enums.OrderStatus.PROCESSING);
                            paymentRepository.save(payment);
                        });
            }
        }

        return ResponseEntity.ok("Webhook processed");
    }
}