package com.capstone.shoppingcart.repositories;

import com.capstone.shoppingcart.entities.Cart;
import com.capstone.shoppingcart.entities.CartItem;
import com.capstone.shoppingcart.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Optional<CartItem> findByCartAndProduct(Cart cart, Product product);
}
