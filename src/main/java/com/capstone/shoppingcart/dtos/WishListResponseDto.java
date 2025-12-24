package com.capstone.shoppingcart.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class WishListResponseDto {
    private UUID id;
    private UUID userId;
    private List<ProductDto> products;
    private LocalDateTime createdAt;
}
