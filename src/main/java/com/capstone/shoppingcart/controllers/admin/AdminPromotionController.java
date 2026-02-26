package com.capstone.shoppingcart.controllers.admin;

import com.capstone.shoppingcart.dtos.CreatePromotionRequest;
import com.capstone.shoppingcart.dtos.PromotionResponseDto;
import com.capstone.shoppingcart.dtos.UpdatePromotionRequest;
import com.capstone.shoppingcart.services.admin.AdminPromotionService;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/v1/promotions")
public class AdminPromotionController {

    private final AdminPromotionService adminPromotionService;

    public AdminPromotionController(AdminPromotionService adminPromotionService) {
        this.adminPromotionService = adminPromotionService;
    }

    @GetMapping
    public ResponseEntity<Page<PromotionResponseDto>> getAllPromotions(Pageable pageable) {
        Page<PromotionResponseDto> promotions = adminPromotionService.getAllPromotions(pageable);
        return ResponseEntity.ok(promotions);
    }

    @PostMapping
    public ResponseEntity<PromotionResponseDto> createPromotion(@Valid @RequestBody CreatePromotionRequest request) {
        PromotionResponseDto promotion = adminPromotionService.createPromotion(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(promotion);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PromotionResponseDto> updatePromotion(
            @PathVariable Long id,
            @Valid @RequestBody UpdatePromotionRequest request) {
        PromotionResponseDto promotion = adminPromotionService.updatePromotion(id, request);
        return ResponseEntity.ok(promotion);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePromotion(@PathVariable Long id) {
        adminPromotionService.deletePromotion(id);
        return ResponseEntity.ok("Promotion deleted successfully");
    }
}
