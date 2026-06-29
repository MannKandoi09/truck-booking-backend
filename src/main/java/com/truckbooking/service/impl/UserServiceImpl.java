package com.truckbooking.service.impl;

import com.truckbooking.dto.request.RegisterRequest;
import com.truckbooking.dto.response.UserResponse;
import com.truckbooking.entity.User;
import com.truckbooking.enums.Role;
import com.truckbooking.repository.UserRepository;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public ApiResponse<?> registerUser(RegisterRequest request) {

        // Check if email already exists
        if (userRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Email already exists")
                    .data(null)
                    .build();
        }

        // Check if phone already exists
        if (userRepository.existsByPhone(request.getPhone())) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Phone number already exists")
                    .data(null)
                    .build();
        }

        // Check password confirmation
        if (!request.getPassword().equals(request.getConfirmPassword())) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Password and Confirm Password do not match")
                    .data(null)
                    .build();
        }

        // Create User Entity
        User user = User.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .enabled(true)
                .build();

        // Save User
        user = userRepository.save(user);

        // Prepare Response DTO
        UserResponse response = UserResponse.builder()
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .phone(user.getPhone())
                .role(user.getRole())
                .enabled(user.getEnabled())
                .build();

        return ApiResponse.builder()
                .success(true)
                .message("User Registered Successfully")
                .data(response)
                .build();
    }
}