package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.CartResponseDto;
import com.capstone.shoppingcart.entities.Cart;
import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.mappers.CartMapper;
import com.capstone.shoppingcart.repositories.CartRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartMapper cartMapper;
    
    public CartService(CartRepository cartRepository, CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartMapper = cartMapper;
    }

    public CartResponseDto getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        return cartMapper.toDto(cart);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }
}
