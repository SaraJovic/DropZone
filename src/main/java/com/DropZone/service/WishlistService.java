package com.DropZone.service;

import com.DropZone.dto.response.WishlistItemResponse;
import com.DropZone.dto.response.WishlistResponse;
import com.DropZone.entity.Product;
import com.DropZone.entity.User;
import com.DropZone.entity.Wishlist;
import com.DropZone.entity.WishlistItem;
import com.DropZone.repository.ProductRepository;
import com.DropZone.repository.UserRepository;
import com.DropZone.repository.WishlistItemRepository;
import com.DropZone.repository.WishlistRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishlistService {

    private final WishlistRepository wishlistRepository;
    private final WishlistItemRepository wishlistItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public WishlistResponse getWishlistByUserId(Long userId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found"));
        return mapToWishlistResponse(wishlist);
    }

    @Transactional
    public WishlistResponse addItemToWishlist(Long userId, Long productId) {
        Wishlist wishlist = wishlistRepository.findByUserId(userId)
                .orElseGet(() -> createWishlistForUser(userId));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (wishlistItemRepository.existsByWishlistIdAndProductId(wishlist.getId(), productId)) {
            throw new RuntimeException("Product already in wishlist");
        }

        WishlistItem item = WishlistItem.builder()
                .wishlist(wishlist)
                .product(product)
                .build();

        wishlistItemRepository.save(item);

        return mapToWishlistResponse(wishlistRepository.findById(wishlist.getId()).get());
    }

    @Transactional
    public void removeItemFromWishlist(Long wishlistItemId) {
        if (!wishlistItemRepository.existsById(wishlistItemId)) {
            throw new RuntimeException("Wishlist item not found");
        }
        wishlistItemRepository.deleteById(wishlistItemId);
    }

    private Wishlist createWishlistForUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Wishlist wishlist = Wishlist.builder()
                .user(user)
                .build();
        return wishlistRepository.save(wishlist);
    }

    private WishlistResponse mapToWishlistResponse(Wishlist wishlist) {
        List<WishlistItemResponse> items = wishlist.getItems().stream()
                .map(this::mapToWishlistItemResponse)
                .collect(Collectors.toList());

        return WishlistResponse.builder()
                .id(wishlist.getId())
                .items(items)
                .build();
    }

    private WishlistItemResponse mapToWishlistItemResponse(WishlistItem item) {
        String primaryImageUrl = item.getProduct().getImages().stream()
                .filter(img -> img.getIsPrimary())
                .map(img -> img.getImageUrl())
                .findFirst()
                .orElse(null);

        return WishlistItemResponse.builder()
                .id(item.getId())
                .productId(item.getProduct().getId())
                .productName(item.getProduct().getName())
                .price(item.getProduct().getPrice())
                .primaryImageUrl(primaryImageUrl)
                .build();
    }
}