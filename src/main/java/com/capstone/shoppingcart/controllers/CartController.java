package com.capstone.shoppingcart.controllers;

import com.capstone.shoppingcart.dtos.AddItemToCartRequest;
import com.capstone.shoppingcart.dtos.CartResponseDto;
import com.capstone.shoppingcart.dtos.UpdateCartItemRequest;
import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.repositories.UserRepository;
import com.capstone.shoppingcart.services.CartService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
public class CartController {

    private final CartService cartService;
    private final UserRepository userRepository;

    public CartController(CartService cartService, UserRepository userRepository) {
        this.cartService = cartService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<CartResponseDto> getMyCart(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CartResponseDto cart = cartService.getOrCreateCart(user);
        return ResponseEntity.ok(cart);
    }

    @PostMapping("/items")
    public ResponseEntity<CartResponseDto> addItemToCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @Valid @RequestBody AddItemToCartRequest request) {
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CartResponseDto cart = cartService.addItemToCart(user, request);
        return ResponseEntity.ok(cart);
    }

    @PutMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDto> updateCartItemQuantity(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId,
            @Valid @RequestBody UpdateCartItemRequest request) {
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CartResponseDto cart = cartService.updateCartItemQuantity(user, itemId, request);
        return ResponseEntity.ok(cart);
    }

    @DeleteMapping("/items/{itemId}")
    public ResponseEntity<CartResponseDto> removeItemFromCart(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long itemId) {
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        CartResponseDto cart = cartService.removeItemFromCart(user, itemId);
        return ResponseEntity.ok(cart);
    }
}
