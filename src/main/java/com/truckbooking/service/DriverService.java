package com.truckbooking.service;

import com.truckbooking.dto.request.DriverRequest;
import com.truckbooking.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface DriverService {

    // Add Driver
    ApiResponse<?> addDriver(DriverRequest request);

    // Get All Drivers
    ApiResponse<?> getAllDrivers();

    // Get Driver By Id
    ApiResponse<?> getDriverById(Long id);

    // Update Driver
    ApiResponse<?> updateDriver(Long id, DriverRequest request);

    // Soft Delete Driver
    ApiResponse<?> deleteDriver(Long id);

    // Upload Driver Image
    ApiResponse<?> uploadDriverImage(Long driverId, MultipartFile file);

}