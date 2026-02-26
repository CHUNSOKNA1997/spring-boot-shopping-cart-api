package com.capstone.shoppingcart.repositories;

import com.capstone.shoppingcart.entities.Promotion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PromotionRepository extends JpaRepository<Promotion, Long> {

    @Query("SELECT p FROM Promotion p " +
            "WHERE p.isActive = true " +
            "AND (p.startAt IS NULL OR p.startAt <= :now) " +
            "AND (p.endAt IS NULL OR p.endAt >= :now) " +
            "ORDER BY p.displayOrder ASC, p.createdAt DESC")
    List<Promotion> findActivePromotions(@Param("now") LocalDateTime now);
}
