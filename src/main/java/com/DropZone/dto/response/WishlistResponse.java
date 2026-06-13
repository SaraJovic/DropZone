package com.DropZone.dto.response;

import lombok.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishlistResponse {
    private Long id;
    private List<WishlistItemResponse> items;
}