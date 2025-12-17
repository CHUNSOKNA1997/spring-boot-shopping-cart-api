package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.AddItemToCartRequest;
import com.capstone.shoppingcart.dtos.CartResponseDto;
import com.capstone.shoppingcart.entities.Cart;
import com.capstone.shoppingcart.entities.CartItem;
import com.capstone.shoppingcart.entities.Product;
import com.capstone.shoppingcart.entities.User;
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
    
    public CartService(CartRepository cartRepository, 
                      CartItemRepository cartItemRepository,
                      ProductRepository productRepository,
                      CartMapper cartMapper) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.cartMapper = cartMapper;
    }

    public CartResponseDto getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        return cartMapper.toDto(cart);
    }

    @Transactional
    public CartResponseDto addItemToCart(User user, AddItemToCartRequest request) {
        // 1. Get or create user's cart
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        // 2. Check if product exists
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        // 3. Check if item already in cart
        CartItem cartItem = cartItemRepository.findByCartAndProduct(cart, product)
                .orElse(null);
        
        if (cartItem != null) {
            // Item exists - add to existing quantity
            cartItem.setQuantity(cartItem.getQuantity() + request.getQuantity());
        } else {
            // Item doesn't exist - create new cart item
            cartItem = new CartItem();
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(request.getQuantity());
            cartItem.setPrice(product.getPrice());
            cart.getCartItems().add(cartItem);
        }
        
        // 4. Update cart timestamp
        cart.setUpdatedAt(LocalDateTime.now());
        
        // 5. Save cart (cascade will save cart items)
        Cart savedCart = cartRepository.save(cart);
        
        // 6. Return updated cart as DTO
        return cartMapper.toDto(savedCart);
    }

    private Cart createNewCart(User user) {
        Cart cart = new Cart();
        cart.setUser(user);
        cart.setCreatedAt(LocalDateTime.now());
        return cartRepository.save(cart);
    }
}
