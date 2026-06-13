package com.DropZone.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemRequest {

    @NotNull
    private Long productVariantId;

    @NotNull
    @Min(1)
    private Integer quantity;
}