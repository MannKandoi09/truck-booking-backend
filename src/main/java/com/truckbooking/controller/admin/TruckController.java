package com.truckbooking.controller.admin;

import com.truckbooking.dto.request.TruckRequest;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.TruckService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/trucks")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class TruckController {

    private final TruckService truckService;

    // Add Truck
    @PostMapping
    public ApiResponse<?> addTruck(@Valid @RequestBody TruckRequest request) {
        return truckService.addTruck(request);
    }

    // Get All Trucks
    @GetMapping
    public ApiResponse<?> getAllTrucks() {
        return truckService.getAllTrucks();
    }

    // Get Truck By Id
    @GetMapping("/{id}")
    public ApiResponse<?> getTruckById(@PathVariable Long id) {
        return truckService.getTruckById(id);
    }

    // Update Truck
    @PutMapping("/{id}")
    public ApiResponse<?> updateTruck(
            @PathVariable Long id,
            @Valid @RequestBody TruckRequest request) {

        return truckService.updateTruck(id, request);
    }

    // Upload Truck Image
    @PostMapping("/{id}/upload-image")
    public ApiResponse<?> uploadTruckImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        return truckService.uploadTruckImage(id, file);
    }

    // Soft Delete Truck
    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteTruck(@PathVariable Long id) {
        return truckService.deleteTruck(id);
    }
}