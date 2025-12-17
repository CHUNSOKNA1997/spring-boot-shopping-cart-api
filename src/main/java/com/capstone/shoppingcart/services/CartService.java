package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.CartItemDto;
import com.capstone.shoppingcart.dtos.CartResponseDto;
import com.capstone.shoppingcart.dtos.ProductDto;
import com.capstone.shoppingcart.entities.Cart;
import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@Service
public class CartService {
    private final CartRepository cartRepository;
    
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartResponseDto getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        return convertToDto(cart);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }

    private CartResponseDto convertToDto(Cart cart) {
        return CartResponseDto.builder()
                .id(cart.getId())
                .createdAt(cart.getCreatedAt())
                .updatedAt(cart.getUpdatedAt())
                .cartItems(cart.getCartItems().stream()
                        .map(item -> CartItemDto.builder()
                                .id(item.getId())
                                .quantity(item.getQuantity())
                                .price(item.getPrice())
                                .product(ProductDto.builder()
                                        .id(item.getProduct().getId())
                                        .name(item.getProduct().getName())
                                        .price(item.getProduct().getPrice())
                                        .build())
                                .build())
                        .collect(Collectors.toList()))
                .build();
    }
}
