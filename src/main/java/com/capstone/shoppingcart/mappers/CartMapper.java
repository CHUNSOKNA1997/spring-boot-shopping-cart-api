package com.capstone.shoppingcart.mappers;

import com.capstone.shoppingcart.dtos.CartItemDto;
import com.capstone.shoppingcart.dtos.CartResponseDto;
import com.capstone.shoppingcart.entities.Cart;
import com.capstone.shoppingcart.entities.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {ProductMapper.class})
public interface CartMapper {
    
    @Mapping(source = "user.id", target = "userId")
    CartResponseDto toDto(Cart cart);
    
    CartItemDto toDto(CartItem cartItem);
}
