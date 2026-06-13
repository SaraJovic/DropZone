package com.DropZone.dto.response;

import lombok.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewResponse {
    private Long id;
    private Integer rating;
    private String comment;
    private String userName;
    private LocalDateTime createdAt;
}