package com.DropZone.controller;

import com.DropZone.dto.response.WishlistResponse;
import com.DropZone.security.SecurityUtils;
import com.DropZone.service.WishlistService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/wishlist")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class WishlistController {

    private final WishlistService wishlistService;
    private final SecurityUtils securityUtils;

    @GetMapping
    public ResponseEntity<WishlistResponse> getWishlist() {
        Long userId = securityUtils.getCurrentUser().getId();
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }

    @PostMapping("/items/{productId}")
    public ResponseEntity<WishlistResponse> addItemToWishlist(@PathVariable Long productId) {
        Long userId = securityUtils.getCurrentUser().getId();
        return ResponseEntity.ok(wishlistService.addItemToWishlist(userId, productId));
    }

    @DeleteMapping("/items/{wishlistItemId}")
    public ResponseEntity<Void> removeItemFromWishlist(@PathVariable Long wishlistItemId) {
        wishlistService.removeItemFromWishlist(wishlistItemId);
        return ResponseEntity.noContent().build();
    }
}