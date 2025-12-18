package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.AddressResponseDto;
import com.capstone.shoppingcart.dtos.CreateAddressRequest;
import com.capstone.shoppingcart.dtos.UpdateAddressRequest;
import com.capstone.shoppingcart.entities.Address;
import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.repositories.AddressRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class AddressService {

    private final AddressRepository addressRepository;

    public AddressService(AddressRepository addressRepository) {
        this.addressRepository = addressRepository;
    }

    /**
     * Get all addresses for authenticated user
     * @param user - The authenticated user
     * @return List of user's addresses
     */
    public List<AddressResponseDto> getAllAddresses(User user) {
        List<Address> addresses = addressRepository.findByUser(user);
        return addresses.stream()
                .map(this::toDto)
                .collect(Collectors.toList());
    }

    /**
     * Create new address for user
     * @param user - The authenticated user
     * @param request - Address data
     * @return Created address
     */
    @Transactional
    public AddressResponseDto createAddress(User user, CreateAddressRequest request) {
        // If this is the first address or marked as default, set as default
        boolean shouldBeDefault = request.getIsDefault() != null && request.getIsDefault();
        
        // If user wants this as default, unset other default addresses
        if (shouldBeDefault) {
            unsetDefaultAddress(user);
        }
        
        // If this is user's first address, make it default automatically
        List<Address> existingAddresses = addressRepository.findByUser(user);
        if (existingAddresses.isEmpty()) {
            shouldBeDefault = true;
        }
        
        Address address = new Address();
        address.setUser(user);
        address.setStreet(request.getStreet());
        address.setCity(request.getCity());
        address.setState(request.getState());
        address.setZipCode(request.getZipCode());
        address.setCountry(request.getCountry());
        address.setIsDefault(shouldBeDefault);
        
        Address savedAddress = addressRepository.save(address);
        return toDto(savedAddress);
    }

    /**
     * Update existing address
     * @param user - The authenticated user
     * @param addressId - Address ID to update
     * @param request - Updated address data
     * @return Updated address
     */
    @Transactional
    public AddressResponseDto updateAddress(User user, UUID addressId, UpdateAddressRequest request) {
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        
        // Update fields if provided
        if (request.getStreet() != null) {
            address.setStreet(request.getStreet());
        }
        if (request.getCity() != null) {
            address.setCity(request.getCity());
        }
        if (request.getState() != null) {
            address.setState(request.getState());
        }
        if (request.getZipCode() != null) {
            address.setZipCode(request.getZipCode());
        }
        if (request.getCountry() != null) {
            address.setCountry(request.getCountry());
        }
        
        // Handle default address update
        if (request.getIsDefault() != null && request.getIsDefault()) {
            unsetDefaultAddress(user);
            address.setIsDefault(true);
        }
        
        Address savedAddress = addressRepository.save(address);
        return toDto(savedAddress);
    }

    /**
     * Delete address
     * @param user - The authenticated user
     * @param addressId - Address ID to delete
     */
    @Transactional
    public void deleteAddress(User user, UUID addressId) {
        Address address = addressRepository.findByIdAndUser(addressId, user)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));
        
        boolean wasDefault = address.getIsDefault() != null && address.getIsDefault();
        
        addressRepository.delete(address);
        
        // If deleted address was default, set another address as default
        if (wasDefault) {
            List<Address> remainingAddresses = addressRepository.findByUser(user);
            if (!remainingAddresses.isEmpty()) {
                Address newDefault = remainingAddresses.get(0);
                newDefault.setIsDefault(true);
                addressRepository.save(newDefault);
            }
        }
    }

    /**
     * Unset all default addresses for user
     * Helper method to ensure only one default address
     * @param user - The authenticated user
     */
    private void unsetDefaultAddress(User user) {
        addressRepository.findByUserAndIsDefaultTrue(user).ifPresent(address -> {
            address.setIsDefault(false);
            addressRepository.save(address);
        });
    }

    /**
     * Convert Address entity to DTO
     * @param address - Address entity
     * @return AddressResponseDto
     */
    private AddressResponseDto toDto(Address address) {
        return AddressResponseDto.builder()
                .id(address.getId())
                .street(address.getStreet())
                .city(address.getCity())
                .state(address.getState())
                .zipCode(address.getZipCode())
                .country(address.getCountry())
                .isDefault(address.getIsDefault())
                .build();
    }
}
