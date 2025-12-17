package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.AddItemToCartRequest;
import com.capstone.shoppingcart.dtos.CartResponseDto;
import com.capstone.shoppingcart.dtos.UpdateCartItemRequest;
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

    /**
     * Get user's cart or create new one if doesn't exist
     * @param user - The authenticated user
     * @return CartResponseDto containing cart details and items
     */
    public CartResponseDto getOrCreateCart(User user) {
        Cart cart = cartRepository.findByUser(user)
                .orElseGet(() -> createNewCart(user));
        
        return cartMapper.toDto(cart);
    }

    /**
     * Add item to cart
     * @param user - The authenticated user
     * @param request - Contains productId and quantity to add
     * @return Updated cart with the added item
     */
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
            cartItem = CartItem.createNew(cart, product, request.getQuantity());
            cart.getCartItems().add(cartItem);
        }
        
        cart.setUpdatedAt(LocalDateTime.now());
        
        Cart savedCart = cartRepository.save(cart);
        
        return cartMapper.toDto(savedCart);
    }

    /**
     * Update cart item quantity
     * @param user - The authenticated user
     * @param itemId - The cart item ID to update
     * @param request - Contains new quantity
     * @return Updated cart
     */
    @Transactional
    public CartResponseDto updateCartItemQuantity(User user, Long itemId, UpdateCartItemRequest request) {
        CartItem cartItem = cartItemRepository.findById(itemId)
                .orElseThrow(() -> new IllegalArgumentException("Cart item not found"));

        if (!cartItem.getCart().getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("Cart item not found");
        }

        cartItem.setQuantity(request.getQuantity());

        Cart cart = cartItem.getCart();
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
