package com.capstone.shoppingcart.repositories;

import com.capstone.shoppingcart.entities.Address;
import com.capstone.shoppingcart.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface AddressRepository extends JpaRepository<Address, UUID> {
    
    // Find all addresses for a specific user
    List<Address> findByUser(User user);
    
    // Find user's default address
    Optional<Address> findByUserAndIsDefaultTrue(User user);
    
    // Find address by ID and user (for security - ensure address belongs to user)
    Optional<Address> findByIdAndUser(UUID id, User user);
}
