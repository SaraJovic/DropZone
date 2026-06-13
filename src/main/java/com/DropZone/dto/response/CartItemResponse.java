package com.DropZone.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {
    private Long id;
    private Long productVariantId;
    private String productName;
    private String color;
    private String size;
    private Integer quantity;
    private java.math.BigDecimal price;
}