package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.ProductResponseDto;
import com.capstone.shoppingcart.dtos.WishListResponseDto;
import com.capstone.shoppingcart.entities.Product;
import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.entities.WishList;
import com.capstone.shoppingcart.mappers.ProductMapper;
import com.capstone.shoppingcart.repositories.ProductRepository;
import com.capstone.shoppingcart.repositories.WishListRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WishListService {

    private final WishListRepository wishListRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    public WishListService(WishListRepository wishListRepository, 
                          ProductRepository productRepository,
                          ProductMapper productMapper) {
        this.wishListRepository = wishListRepository;
        this.productRepository = productRepository;
        this.productMapper = productMapper;
    }

    /**
     * Get user's wishlist
     * @param user - The authenticated user
     * @return User's wishlist with products
     */
    public WishListResponseDto getWishList(User user) {
        WishList wishList = wishListRepository.findByUser(user)
                .orElseGet(() -> createNewWishList(user));
        
        return toDto(wishList);
    }

    /**
     * Add product to wishlist
     * @param user - The authenticated user
     * @param productId - Product ID to add
     * @return Updated wishlist
     */
    @Transactional
    public WishListResponseDto addToWishList(User user, Long productId) {
        WishList wishList = wishListRepository.findByUser(user)
                .orElseGet(() -> createNewWishList(user));
        
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        // Check if product already in wishlist
        if (wishList.getProducts() == null) {
            wishList.setProducts(new ArrayList<>());
        }
        
        boolean alreadyExists = wishList.getProducts().stream()
                .anyMatch(p -> p.getId().equals(productId));
        
        if (alreadyExists) {
            throw new IllegalArgumentException("Product already in wishlist");
        }
        
        wishList.getProducts().add(product);
        WishList savedWishList = wishListRepository.save(wishList);
        
        return toDto(savedWishList);
    }

    /**
     * Remove product from wishlist
     * @param user - The authenticated user
     * @param productId - Product ID to remove
     * @return Updated wishlist
     */
    @Transactional
    public WishListResponseDto removeFromWishList(User user, Long productId) {
        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist not found"));
        
        if (wishList.getProducts() == null) {
            throw new IllegalArgumentException("Product not in wishlist");
        }
        
        boolean removed = wishList.getProducts().removeIf(p -> p.getId().equals(productId));
        
        if (!removed) {
            throw new IllegalArgumentException("Product not in wishlist");
        }
        
        WishList savedWishList = wishListRepository.save(wishList);
        return toDto(savedWishList);
    }

    /**
     * Clear entire wishlist
     * @param user - The authenticated user
     */
    @Transactional
    public void clearWishList(User user) {
        WishList wishList = wishListRepository.findByUser(user)
                .orElseThrow(() -> new IllegalArgumentException("Wishlist not found"));
        
        if (wishList.getProducts() != null) {
            wishList.getProducts().clear();
            wishListRepository.save(wishList);
        }
    }

    /**
     * Create new wishlist for user
     * @param user - The authenticated user
     * @return New wishlist
     */
    private WishList createNewWishList(User user) {
        WishList wishList = new WishList();
        wishList.setUser(user);
        wishList.setCreatedAt(LocalDateTime.now());
        wishList.setProducts(new ArrayList<>());
        return wishListRepository.save(wishList);
    }

    /**
     * Convert WishList entity to DTO
     * @param wishList - WishList entity
     * @return WishListResponseDto
     */
    private WishListResponseDto toDto(WishList wishList) {
        List<ProductResponseDto> productDtos = wishList.getProducts() != null
                ? wishList.getProducts().stream()
                        .map(productMapper::toDto)
                        .collect(Collectors.toList())
                : new ArrayList<>();
        
        return WishListResponseDto.builder()
                .id(wishList.getId())
                .userId(wishList.getUser().getId())
                .products(productDtos)
                .createdAt(wishList.getCreatedAt())
                .build();
    }
}
