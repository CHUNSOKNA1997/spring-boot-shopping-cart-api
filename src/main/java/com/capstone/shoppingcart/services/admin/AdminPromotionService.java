package com.capstone.shoppingcart.services.admin;

import com.capstone.shoppingcart.dtos.CreatePromotionRequest;
import com.capstone.shoppingcart.dtos.PromotionResponseDto;
import com.capstone.shoppingcart.dtos.UpdatePromotionRequest;
import com.capstone.shoppingcart.entities.Promotion;
import com.capstone.shoppingcart.mappers.PromotionMapper;
import com.capstone.shoppingcart.repositories.PromotionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class AdminPromotionService {

    private final PromotionRepository promotionRepository;
    private final PromotionMapper promotionMapper;

    public AdminPromotionService(PromotionRepository promotionRepository, PromotionMapper promotionMapper) {
        this.promotionRepository = promotionRepository;
        this.promotionMapper = promotionMapper;
    }

    @Transactional
    public PromotionResponseDto createPromotion(CreatePromotionRequest request) {
        validatePromotionWindow(request.getStartAt(), request.getEndAt());

        Promotion promotion = Promotion.builder()
                .title(request.getTitle())
                .subtitle(request.getSubtitle())
                .description(request.getDescription())
                .discountText(request.getDiscountText())
                .imageUrl(request.getImageUrl())
                .buttonText(request.getButtonText())
                .buttonUrl(request.getButtonUrl())
                .displayOrder(request.getDisplayOrder() != null ? request.getDisplayOrder() : 0)
                .isActive(request.getIsActive() != null ? request.getIsActive() : true)
                .startAt(request.getStartAt())
                .endAt(request.getEndAt())
                .createdAt(LocalDateTime.now())
                .build();

        Promotion savedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(savedPromotion);
    }

    @Transactional
    public PromotionResponseDto updatePromotion(Long id, UpdatePromotionRequest request) {
        Promotion promotion = promotionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Promotion not found"));

        LocalDateTime nextStartAt = request.getStartAt() != null ? request.getStartAt() : promotion.getStartAt();
        LocalDateTime nextEndAt = request.getEndAt() != null ? request.getEndAt() : promotion.getEndAt();
        validatePromotionWindow(nextStartAt, nextEndAt);

        if (request.getTitle() != null) {
            promotion.setTitle(request.getTitle());
        }
        if (request.getSubtitle() != null) {
            promotion.setSubtitle(request.getSubtitle());
        }
        if (request.getDescription() != null) {
            promotion.setDescription(request.getDescription());
        }
        if (request.getDiscountText() != null) {
            promotion.setDiscountText(request.getDiscountText());
        }
        if (request.getImageUrl() != null) {
            promotion.setImageUrl(request.getImageUrl());
        }
        if (request.getButtonText() != null) {
            promotion.setButtonText(request.getButtonText());
        }
        if (request.getButtonUrl() != null) {
            promotion.setButtonUrl(request.getButtonUrl());
        }
        if (request.getDisplayOrder() != null) {
            promotion.setDisplayOrder(request.getDisplayOrder());
        }
        if (request.getIsActive() != null) {
            promotion.setIsActive(request.getIsActive());
        }
        if (request.getStartAt() != null) {
            promotion.setStartAt(request.getStartAt());
        }
        if (request.getEndAt() != null) {
            promotion.setEndAt(request.getEndAt());
        }

        promotion.setUpdatedAt(LocalDateTime.now());
        Promotion updatedPromotion = promotionRepository.save(promotion);
        return promotionMapper.toDto(updatedPromotion);
    }

    @Transactional
    public void deletePromotion(Long id) {
        if (!promotionRepository.existsById(id)) {
            throw new IllegalArgumentException("Promotion not found");
        }

        promotionRepository.deleteById(id);
    }

    public Page<PromotionResponseDto> getAllPromotions(Pageable pageable) {
        return promotionRepository.findAll(pageable).map(promotionMapper::toDto);
    }

    private void validatePromotionWindow(LocalDateTime startAt, LocalDateTime endAt) {
        if (startAt != null && endAt != null && endAt.isBefore(startAt)) {
            throw new IllegalArgumentException("endAt must be after or equal to startAt");
        }
    }
}
