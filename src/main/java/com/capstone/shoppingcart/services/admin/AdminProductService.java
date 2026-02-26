package com.capstone.shoppingcart.services.admin;

import com.capstone.shoppingcart.dtos.CreateProductRequest;
import com.capstone.shoppingcart.dtos.ProductResponseDto;
import com.capstone.shoppingcart.dtos.UpdateProductRequest;
import com.capstone.shoppingcart.entities.Category;
import com.capstone.shoppingcart.entities.Product;
import com.capstone.shoppingcart.mappers.ProductMapper;
import com.capstone.shoppingcart.repositories.CategoryRepository;
import com.capstone.shoppingcart.repositories.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class AdminProductService {
    
    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final ProductMapper productMapper;
    
    /**
     * Create new product
     * @param request - Product data (name, description, price, stock, categoryId)
     * @return Created product with generated ID
     */
    @Transactional
    public ProductResponseDto createProduct(CreateProductRequest request) {
        // 1. Find category (throw error if not exists)
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));
        
        // 2. Create new product
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .imageUrl(request.getImageUrl())
                .category(category)
                .createdAt(LocalDateTime.now())
                .build();
        
        // 3. Save to database
        Product savedProduct = productRepository.save(product);
        
        // 4. Convert to DTO and return
        return productMapper.toDto(savedProduct);
    }
    
    /**
     * Update existing product
     * @param productId - Product ID to update
     * @param request - Updated data (only provided fields will be updated)
     * @return Updated product
     */
    @Transactional
    public ProductResponseDto updateProduct(Long productId, UpdateProductRequest request) {
        // 1. Find product (throw error if not exists)
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with ID: " + productId));
        
        // 2. Update only provided fields
        if (request.getName() != null) {
            product.setName(request.getName());
        }
        if (request.getDescription() != null) {
            product.setDescription(request.getDescription());
        }
        if (request.getPrice() != null) {
            product.setPrice(request.getPrice());
        }
        if (request.getStock() != null) {
            product.setStock(request.getStock());
        }
        if (request.getImageUrl() != null) {
            product.setImageUrl(request.getImageUrl());
        }
        if (request.getCategoryId() != null) {
            Category category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> new IllegalArgumentException("Category not found with ID: " + request.getCategoryId()));
            product.setCategory(category);
        }
        
        // 3. Update timestamp
        product.setUpdatedAt(LocalDateTime.now());
        
        // 4. Save and return
        Product updatedProduct = productRepository.save(product);
        return productMapper.toDto(updatedProduct);
    }
    
    /**
     * Delete product
     * @param productId - Product ID to delete
     */
    @Transactional
    public void deleteProduct(Long productId) {
        // 1. Check if product exists
        if (!productRepository.existsById(productId)) {
            throw new IllegalArgumentException("Product not found with ID: " + productId);
        }
        
        // 2. Delete product
        productRepository.deleteById(productId);
    }
    
    /**
     * Get all products with pagination (admin view)
     * @param pageable - Pagination parameters (page, size, sort)
     * @return Page of products
     */
    public Page<ProductResponseDto> getAllProducts(Pageable pageable) {
        Page<Product> products = productRepository.findAll(pageable);
        return products.map(productMapper::toDto);
    }
}
