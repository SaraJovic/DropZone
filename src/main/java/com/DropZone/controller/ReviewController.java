package com.DropZone.controller;

import com.DropZone.dto.request.ReviewRequest;
import com.DropZone.dto.response.ReviewResponse;
import com.DropZone.security.SecurityUtils;
import com.DropZone.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/reviews")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ReviewController {

    private final ReviewService reviewService;
    private final SecurityUtils securityUtils;

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<ReviewResponse>> getReviewsByProduct(@PathVariable Long productId) {
        return ResponseEntity.ok(reviewService.getReviewsByProduct(productId));
    }

    @PostMapping("/product/{productId}")
    public ResponseEntity<ReviewResponse> createReview(@PathVariable Long productId,
                                                       @Valid @RequestBody ReviewRequest request) {
        Long userId = securityUtils.getCurrentUser().getId();
        return ResponseEntity.ok(reviewService.createReview(userId, productId, request));
    }

    @DeleteMapping("/{reviewId}")
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.noContent().build();
    }
}