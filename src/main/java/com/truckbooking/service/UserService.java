package com.truckbooking.service;

import com.truckbooking.dto.request.RegisterRequest;
import com.truckbooking.response.ApiResponse;

public interface UserService {

    ApiResponse<?> registerUser(RegisterRequest request);

}