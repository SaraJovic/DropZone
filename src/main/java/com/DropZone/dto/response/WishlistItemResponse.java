package com.DropZone.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistItemResponse {
    private Long id;
    private Long productId;
    private String productName;
    private BigDecimal price;
    private String primaryImageUrl;
}