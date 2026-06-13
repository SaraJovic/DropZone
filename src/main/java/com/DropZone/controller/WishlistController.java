package com.DropZone.controller;

import com.DropZone.dto.response.WishlistResponse;
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

    @GetMapping("/{userId}")
    public ResponseEntity<WishlistResponse> getWishlist(@PathVariable Long userId) {
        return ResponseEntity.ok(wishlistService.getWishlistByUserId(userId));
    }

    @PostMapping("/{userId}/items/{productId}")
    public ResponseEntity<WishlistResponse> addItemToWishlist(@PathVariable Long userId,
                                                              @PathVariable Long productId) {
        return ResponseEntity.ok(wishlistService.addItemToWishlist(userId, productId));
    }

    @DeleteMapping("/items/{wishlistItemId}")
    public ResponseEntity<Void> removeItemFromWishlist(@PathVariable Long wishlistItemId) {
        wishlistService.removeItemFromWishlist(wishlistItemId);
        return ResponseEntity.noContent().build();
    }
}