package com.capstone.shoppingcart.mappers;

import com.capstone.shoppingcart.dtos.CategoryResponseDto;
import com.capstone.shoppingcart.entities.Category;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CategoryMapper {
    
    CategoryResponseDto toDto(Category category);
}
