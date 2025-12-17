package com.capstone.shoppingcart.mappers;

import com.capstone.shoppingcart.dtos.ProductDto;
import com.capstone.shoppingcart.entities.Product;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    
    ProductDto toDto(Product product);
}
