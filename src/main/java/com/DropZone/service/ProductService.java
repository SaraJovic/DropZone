package com.DropZone.service;

import com.DropZone.dto.request.ProductRequest;
import com.DropZone.dto.request.ProductVariantRequest;
import com.DropZone.dto.response.ProductImageResponse;
import com.DropZone.dto.response.ProductResponse;
import com.DropZone.dto.response.ProductVariantResponse;
import com.DropZone.entity.Category;
import com.DropZone.entity.Product;
import com.DropZone.entity.ProductImage;
import com.DropZone.entity.ProductVariant;
import com.DropZone.enums.Gender;
import com.DropZone.exception.ResourceNotFoundException;
import com.DropZone.repository.CategoryRepository;
import com.DropZone.repository.ProductImageRepository;
import com.DropZone.repository.ProductRepository;
import com.DropZone.repository.ProductVariantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductVariantRepository productVariantRepository;
    private final ProductImageRepository productImageRepository;

    @Transactional(readOnly = true)
    public Page<ProductResponse> getAllProducts(Pageable pageable) {
        return productRepository.findAll(pageable).map(this::mapToProductResponse);
    }

    @Transactional(readOnly = true)
    public ProductResponse getProductById(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));
        return mapToProductResponse(product);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getProductsByCategory(Long categoryId) {
        return productRepository.findByCategoryId(categoryId)
                .stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name)
                .stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> filterProducts(Long categoryId, Gender gender) {
        List<Product> products;

        if (categoryId != null && gender != null) {
            products = productRepository.findByCategoryIdAndGender(categoryId, gender);
        } else if (categoryId != null) {
            products = productRepository.findByCategoryId(categoryId);
        } else if (gender != null) {
            products = productRepository.findByGender(gender);
        } else {
            products = productRepository.findAll();
        }

        return products.stream().map(this::mapToProductResponse).collect(Collectors.toList());
    }

    @Transactional
    public ProductResponse createProduct(ProductRequest request) {
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .gender(request.getGender())
                .category(category)
                .build();

        return mapToProductResponse(productRepository.save(product));
    }

    @Transactional
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setGender(request.getGender());
        product.setCategory(category);

        return mapToProductResponse(productRepository.save(product));
    }

    @Transactional
    public void deleteProduct(Long id) {
        if (!productRepository.existsById(id)) {
            throw new ResourceNotFoundException("Product not found");
        }
        productRepository.deleteById(id);
    }

    @Transactional
    public ProductVariantResponse addVariant(Long productId, ProductVariantRequest request) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductVariant variant = ProductVariant.builder()
                .size(request.getSize())
                .color(request.getColor())
                .stockQuantity(request.getStockQuantity())
                .product(product)
                .build();

        return mapToVariantResponse(productVariantRepository.save(variant));
    }

    @Transactional
    public ProductImageResponse addImage(Long productId, String imageUrl, Boolean isPrimary) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        ProductImage image = ProductImage.builder()
                .imageUrl(imageUrl)
                .isPrimary(isPrimary)
                .product(product)
                .build();

        return mapToImageResponse(productImageRepository.save(image));
    }

    private ProductResponse mapToProductResponse(Product product) {
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .gender(product.getGender())
                .categoryId(product.getCategory().getId())
                .categoryName(product.getCategory().getName())
                .images(product.getImages() != null ? product.getImages().stream().map(this::mapToImageResponse).collect(Collectors.toList()) : Collections.emptyList())
                .variants(product.getVariants() != null ? product.getVariants().stream().map(this::mapToVariantResponse).collect(Collectors.toList()) : Collections.emptyList())
                .createdAt(product.getCreatedAt())
                .build();
    }

    private ProductVariantResponse mapToVariantResponse(ProductVariant variant) {
        return ProductVariantResponse.builder()
                .id(variant.getId())
                .size(variant.getSize())
                .color(variant.getColor())
                .stockQuantity(variant.getStockQuantity())
                .build();
    }

    private ProductImageResponse mapToImageResponse(ProductImage image) {
        return ProductImageResponse.builder()
                .id(image.getId())
                .imageUrl(image.getImageUrl())
                .isPrimary(image.getIsPrimary())
                .build();
    }
}