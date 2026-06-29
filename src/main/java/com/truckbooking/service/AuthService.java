package com.truckbooking.service;

import com.truckbooking.dto.request.LoginRequest;
import com.truckbooking.response.ApiResponse;

public interface AuthService {

    ApiResponse<?> login(LoginRequest request);

}