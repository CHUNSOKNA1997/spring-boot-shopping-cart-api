package com.capstone.shoppingcart.mappers;

import com.capstone.shoppingcart.dtos.ProductResponseDto;
import com.capstone.shoppingcart.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "category.name", target = "categoryName")
    ProductResponseDto toDto(Product product);
}
