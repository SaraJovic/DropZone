package com.DropZone.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {
    private Long id;
    private String productName;
    private String color;
    private String size;
    private Integer quantity;
    private BigDecimal priceAtPurchase;
}