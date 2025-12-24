package com.capstone.shoppingcart.controllers.customer;

import com.capstone.shoppingcart.dtos.WishListResponseDto;
import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.repositories.UserRepository;
import com.capstone.shoppingcart.services.WishListService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/customer/v1/wishlist")
public class WishListController {

    private final WishListService wishListService;
    private final UserRepository userRepository;

    public WishListController(WishListService wishListService, UserRepository userRepository) {
        this.wishListService = wishListService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<WishListResponseDto> getWishList(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        WishListResponseDto wishList = wishListService.getWishList(user);
        return ResponseEntity.ok(wishList);
    }

    @PostMapping("/products/{productId}")
    public ResponseEntity<WishListResponseDto> addToWishList(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        WishListResponseDto wishList = wishListService.addToWishList(user, productId);
        return ResponseEntity.status(HttpStatus.CREATED).body(wishList);
    }

    @DeleteMapping("/products/{productId}")
    public ResponseEntity<WishListResponseDto> removeFromWishList(
            @AuthenticationPrincipal UserDetails userDetails,
            @PathVariable Long productId) {
        
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        WishListResponseDto wishList = wishListService.removeFromWishList(user, productId);
        return ResponseEntity.ok(wishList);
    }

    @DeleteMapping
    public ResponseEntity<String> clearWishList(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        wishListService.clearWishList(user);
        return ResponseEntity.ok("Wishlist cleared successfully");
    }
}
