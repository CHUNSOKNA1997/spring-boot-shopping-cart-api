package com.capstone.shoppingcart.controllers;

import com.capstone.shoppingcart.dtos.CartResponseDto;
import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.repositories.UserRepository;
import com.capstone.shoppingcart.services.CartService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
