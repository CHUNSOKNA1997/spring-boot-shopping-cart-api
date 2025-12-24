package com.capstone.shoppingcart.repositories;

import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.entities.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishListRepository extends JpaRepository<WishList, UUID> {
    
    // Find wishlist by user
    Optional<WishList> findByUser(User user);
}
