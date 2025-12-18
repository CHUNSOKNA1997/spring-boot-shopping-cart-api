package com.capstone.shoppingcart.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressResponseDto {
    private UUID id;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private Boolean isDefault;
}
