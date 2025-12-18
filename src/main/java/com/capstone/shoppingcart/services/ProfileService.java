package com.capstone.shoppingcart.services;

import com.capstone.shoppingcart.dtos.ChangePasswordRequest;
import com.capstone.shoppingcart.dtos.ProfileResponseDto;
import com.capstone.shoppingcart.dtos.UpdateProfileRequest;
import com.capstone.shoppingcart.entities.Profile;
import com.capstone.shoppingcart.entities.User;
import com.capstone.shoppingcart.repositories.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
public class ProfileService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public ProfileService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    /**
     * Get user profile (combines User and Profile data)
     * @param user - The authenticated user
     * @return User profile information
     */
    public ProfileResponseDto getProfile(User user) {
        Profile profile = user.getProfile();
        
        return ProfileResponseDto.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .firstName(profile != null ? profile.getFirstName() : null)
                .lastName(profile != null ? profile.getLastName() : null)
                .phone(profile != null ? profile.getPhone() : null)
                .avatar(profile != null ? profile.getAvatar() : null)
                .bio(profile != null ? profile.getBio() : null)
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }

    /**
     * Update user profile
     * @param user - The authenticated user
     * @param request - Profile update data
     * @return Updated profile information
     */
    @Transactional
    public ProfileResponseDto updateProfile(User user, UpdateProfileRequest request) {
        // Update User fields if provided
        if (request.getUsername() != null && !request.getUsername().equals(user.getUsername())) {
            if (userRepository.findByUsername(request.getUsername()).isPresent()) {
                throw new IllegalArgumentException("Username already exists");
            }
            user.setUsername(request.getUsername());
        }
        
        if (request.getEmail() != null && !request.getEmail().equals(user.getEmail())) {
            if (userRepository.findByEmail(request.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email already exists");
            }
            user.setEmail(request.getEmail());
        }
        
        // Create or update Profile
        Profile profile = user.getProfile();
        if (profile == null) {
            profile = new Profile();
            profile.setUser(user);
            user.setProfile(profile);
        }
        
        // Update Profile fields if provided
        if (request.getFirstName() != null) {
            profile.setFirstName(request.getFirstName());
        }
        if (request.getLastName() != null) {
            profile.setLastName(request.getLastName());
        }
        if (request.getPhone() != null) {
            profile.setPhone(request.getPhone());
        }
        if (request.getAvatar() != null) {
            profile.setAvatar(request.getAvatar());
        }
        if (request.getBio() != null) {
            profile.setBio(request.getBio());
        }
        
        user.setUpdatedAt(LocalDateTime.now());
        User savedUser = userRepository.save(user);
        
        return getProfile(savedUser);
    }

    /**
     * Change user password
     * @param user - The authenticated user
     * @param request - Contains current and new password
     */
    @Transactional
    public void changePassword(User user, ChangePasswordRequest request) {
        // Verify current password
        if (!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Current password is incorrect");
        }
        
        // Update to new password
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        user.setUpdatedAt(LocalDateTime.now());
        userRepository.save(user);
    }
}
