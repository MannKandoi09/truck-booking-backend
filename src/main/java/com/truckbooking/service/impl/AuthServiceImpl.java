package com.truckbooking.service.impl;

import com.truckbooking.dto.request.LoginRequest;
import com.truckbooking.dto.response.LoginResponse;
import com.truckbooking.entity.User;
import com.truckbooking.repository.UserRepository;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.security.jwt.JwtService;
import com.truckbooking.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Override
    public ApiResponse<?> login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String token = jwtService.generateToken(user);

        LoginResponse response = LoginResponse.builder()
                .token(token)
                .type("Bearer")
                .id(user.getId())
                .firstName(user.getFirstName())
                .lastName(user.getLastName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();

        return ApiResponse.builder()
                .success(true)
                .message("Login Successful")
                .data(response)
                .build();
    }
}