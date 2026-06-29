package com.truckbooking.service.impl;

import com.truckbooking.entity.User;
import com.truckbooking.enums.Role;
import com.truckbooking.repository.UserRepository;
import com.truckbooking.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void createDefaultAdmin() {

        if (userRepository.existsByEmail("admin@truckbooking.com")) {
            return;
        }

        User admin = User.builder()
                .firstName("Super")
                .lastName("Admin")
                .email("admin@truckbooking.com")
                .phone("9999999999")
                .password(passwordEncoder.encode("Admin@123"))
                .role(Role.ADMIN)
                .enabled(true)
                .build();

        userRepository.save(admin);

        System.out.println("======================================");
        System.out.println(" Default Admin Created Successfully ");
        System.out.println(" Email : admin@truckbooking.com");
        System.out.println(" Password : Admin@123");
        System.out.println("======================================");
    }
}