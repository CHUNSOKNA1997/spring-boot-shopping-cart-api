package com.capstone.shoppingcart.mappers;

import com.capstone.shoppingcart.dtos.PromotionResponseDto;
import com.capstone.shoppingcart.entities.Promotion;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface PromotionMapper {

    PromotionResponseDto toDto(Promotion promotion);
}
