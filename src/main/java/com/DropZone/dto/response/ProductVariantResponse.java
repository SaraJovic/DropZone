package com.DropZone.dto.response;

import com.DropZone.enums.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantResponse {
    private Long id;
    private Size size;
    private String color;
    private Integer stockQuantity;
}
