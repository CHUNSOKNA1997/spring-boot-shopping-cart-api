package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.PromotionResponseDto;
import com.capstone.shoppingcart.entities.Promotion;
import com.capstone.shoppingcart.mappers.PromotionMapper;
import com.capstone.shoppingcart.repositories.PromotionRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public PromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    public List<PromotionResponseDto> getActivePromotions() {
        return promotionRepository.findActivePromotions(LocalDateTime.now())
                .stream()
                .map(promotionMapper::toDto)
                .toList();
    }

    public PromotionResponseDto getPromotionById(Long id) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));

        LocalDateTime now = LocalDateTime.now();
        boolean notStarted = promotion.getStartAt() != null && promotion.getStartAt().isAfter(now);
        boolean expired = promotion.getEndAt() != null && promotion.getEndAt().isBefore(now);
        boolean inactive = Boolean.FALSE.equals(promotion.getIsActive());

        if (inactive || notStarted || expired) {
            throw new IllegalArgumentException("Promotion not found");
        }

        return promotionMapper.toDto(promotion);
    }
}
