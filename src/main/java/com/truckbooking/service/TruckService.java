package com.truckbooking.service;

import com.truckbooking.dto.request.TruckRequest;
import com.truckbooking.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface TruckService {

    // Add Truck
    ApiResponse<?> addTruck(TruckRequest request);

    // Get All Trucks
    ApiResponse<?> getAllTrucks();

    // Get Truck By Id
    ApiResponse<?> getTruckById(Long id);

    // Update Truck
    ApiResponse<?> updateTruck(Long id, TruckRequest request);

    // Soft Delete Truck
    ApiResponse<?> deleteTruck(Long id);

    // Upload Truck Image
    ApiResponse<?> uploadTruckImage(Long truckId, MultipartFile file);

}