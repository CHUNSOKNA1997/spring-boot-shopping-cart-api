package com.capstone.shoppingcart.dtos;

import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class UpdatePromotionRequest {

    @Size(max = 120, message = "Title must not exceed 120 characters")
    private String title;

    @Size(max = 255, message = "Subtitle must not exceed 255 characters")
    private String subtitle;

    @Size(max = 1000, message = "Description must not exceed 1000 characters")
    private String description;

    @Size(max = 80, message = "Discount text must not exceed 80 characters")
    private String discountText;

    private String imageUrl;

    @Size(max = 40, message = "Button text must not exceed 40 characters")
    private String buttonText;

    private String buttonUrl;

    private Integer displayOrder;

    private Boolean isActive;

    private LocalDateTime startAt;

    private LocalDateTime endAt;
}
