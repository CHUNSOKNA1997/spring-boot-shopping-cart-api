package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.AddItemToCartRequest;
import com.capstone.shoppingcart.dtos.CartResponseDto;
import com.capstone.shoppingcart.entities.Cart;
import com.capstone.shoppingcart.entities.CartItem;
import com.capstone.shoppingcart.entities.Product;
import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.mappers.CartItemMapper;
import com.capstone.shoppingcart.mappers.CartMapper;
import com.capstone.shoppingcart.repositories.CartItemRepository;
import com.capstone.shoppingcart.repositories.CartRepository;
import com.capstone.shoppingcart.repositories.ProductRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;
    private final CartItemMapper cartItemMapper;
    
    public CartService(CartRepository cartRepository, 
                      CartItemRepository cartItemRepository,
                      ProductRepository productRepository,
                      CartMapper cartMapper,
                      CartItemMapper cartItemMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
        this.cartItemMapper = cartItemMapper;
    }

    public CartResponseDto getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartResponseDto addItemToCart(User user, AddItemToCartRequest request) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);
        
        if (cartItem != null) {
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            cartItem = cartItemMapper.toEntity(request, cart, product);
            cart.getCartItems().add(cartItem);
        }
        
        cart.setUpdatedAt(LocalDateTime.now());
        
        Cart savedCart = cartRepository.save(cart);
        
        return cartMapper.toDto(savedCart);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }
}
