package com.truckbooking.controller.admin;

import com.truckbooking.dto.request.DriverRequest;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.DriverService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/drivers")
@RequiredArgsConstructor
public class DriverController {

    private final DriverService driverService;

    // Add Driver
    @PostMapping
    public ApiResponse<?> addDriver(@Valid @RequestBody DriverRequest request) {
        return driverService.addDriver(request);
    }

    // Get All Drivers
    @GetMapping
    public ApiResponse<?> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    // Get Driver By Id
    @GetMapping("/{id}")
    public ApiResponse<?> getDriverById(@PathVariable Long id) {
        return driverService.getDriverById(id);
    }

    // Update Driver
    @PutMapping("/{id}")
    public ApiResponse<?> updateDriver(
            @PathVariable Long id,
            @Valid @RequestBody DriverRequest request) {

        return driverService.updateDriver(id, request);
    }

    // Upload Driver Image
    @PostMapping("/{id}/upload-image")
    public ApiResponse<?> uploadDriverImage(
            @PathVariable Long id,
            @RequestParam("file") MultipartFile file) {

        return driverService.uploadDriverImage(id, file);
    }

    // Delete Driver
    @DeleteMapping("/{id}")
    public ApiResponse<?> deleteDriver(@PathVariable Long id) {
        return driverService.deleteDriver(id);
    }
}