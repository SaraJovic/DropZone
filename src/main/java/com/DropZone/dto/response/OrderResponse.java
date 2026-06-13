package com.DropZone.dto.response;

import com.DropZone.enums.OrderStatus;
import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {
    private Long id;
    private OrderStatus status;
    private BigDecimal totalPrice;
    private String shippingAddress;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
}