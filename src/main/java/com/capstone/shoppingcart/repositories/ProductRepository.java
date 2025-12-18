package com.capstone.shoppingcart.repositories;

import com.capstone.shoppingcart.entities.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    // Find products by category
    Page<Product> findByCategory_Id(Long categoryId, Pageable pageable);
    
    // Search products by name (case-insensitive, partial match)
    @Query("SELECT p FROM Product p WHERE LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%'))")
    Page<Product> searchProducts(@Param("search") String search, Pageable pageable);
    
    // Search products by category and name
    @Query("SELECT p FROM Product p WHERE p.category.id = :categoryId " +
           "AND (LOWER(p.name) LIKE LOWER(CONCAT('%', :search, '%')) " +
           "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :search, '%')))")
    Page<Product> searchProductsByCategory(@Param("categoryId") Long categoryId, 
                                           @Param("search") String search, 
                                           Pageable pageable);
}
