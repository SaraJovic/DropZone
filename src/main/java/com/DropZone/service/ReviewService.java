package com.DropZone.service;

import com.DropZone.dto.request.ReviewRequest;
import com.DropZone.dto.response.ReviewResponse;
import com.DropZone.entity.Product;
import com.DropZone.entity.Review;
import com.DropZone.entity.User;
import com.DropZone.repository.OrderRepository;
import com.DropZone.repository.ProductRepository;
import com.DropZone.repository.ReviewRepository;
import com.DropZone.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;

    public List<ReviewResponse> getReviewsByProduct(Long productId) {
        return reviewRepository.findByProductId(productId)
                .stream().map(this::mapToReviewResponse).collect(Collectors.toList());
    }

    @Transactional
    public ReviewResponse createReview(Long userId, Long productId, ReviewRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (reviewRepository.existsByUserIdAndProductId(userId, productId)) {
            throw new RuntimeException("You have already reviewed this product");
        }

        boolean hasPurchased = orderRepository.findByUserId(userId).stream()
                .anyMatch(order -> order.getItems().stream()
                        .anyMatch(item -> item.getProductVariant().getProduct().getId().equals(productId)));

        if (!hasPurchased) {
            throw new RuntimeException("You can only review products you have purchased");
        }

        Review review = Review.builder()
                .user(user)
                .product(product)
                .rating(request.getRating())
                .comment(request.getComment())
                .build();

        return mapToReviewResponse(reviewRepository.save(review));
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        if (!reviewRepository.existsById(reviewId)) {
            throw new RuntimeException("Review not found");
        }
        reviewRepository.deleteById(reviewId);
    }

    private ReviewResponse mapToReviewResponse(Review review) {
        return ReviewResponse.builder()
                .id(review.getId())
                .rating(review.getRating())
                .comment(review.getComment())
                .userName(review.getUser().getFirstName() + " " + review.getUser().getLastName())
                .createdAt(review.getCreatedAt())
                .build();
    }
}