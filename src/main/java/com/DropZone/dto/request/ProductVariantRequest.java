package com.DropZone.dto.request;

import com.DropZone.enums.Size;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductVariantRequest {

    @NotNull
    private Size size;

    @NotBlank
    private String color;

    @NotNull
    @Min(0)
    private Integer stockQuantity;
}