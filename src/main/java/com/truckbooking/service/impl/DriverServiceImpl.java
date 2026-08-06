package com.truckbooking.service.impl;

import com.truckbooking.dto.request.DriverRequest;
import com.truckbooking.dto.response.DriverResponse;
import com.truckbooking.entity.Driver;
import com.truckbooking.entity.Truck;
import com.truckbooking.enums.DriverStatus;
import com.truckbooking.repository.DriverRepository;
import com.truckbooking.repository.TruckRepository;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.DriverService;
import com.truckbooking.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DriverServiceImpl implements DriverService {

    private final DriverRepository driverRepository;
    private final TruckRepository truckRepository;
    private final FileStorageService fileStorageService;

    @Override
    public ApiResponse<?> addDriver(DriverRequest request) {

        if (driverRepository.existsByLicenseNumber(request.getLicenseNumber())) {
            return ApiResponse.builder()
                    .success(false)
                    .message("License Number already exists")
                    .data(null)
                    .build();
        }

        if (driverRepository.existsByPhone(request.getPhone())) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Phone Number already exists")
                    .data(null)
                    .build();
        }

        if (driverRepository.existsByEmail(request.getEmail())) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Email already exists")
                    .data(null)
                    .build();
        }

        Truck truck = null;

        if (request.getTruckId() != null) {
            truck = truckRepository.findById(request.getTruckId()).orElse(null);
        }

        Driver driver = Driver.builder()
                .fullName(request.getFullName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .licenseNumber(request.getLicenseNumber())
                .licenseExpiry(request.getLicenseExpiry())
                .experience(request.getExperience())
                .salary(request.getSalary())
                .status(request.getStatus())
                .image(request.getImage())
                .truck(truck)
                .build();

        driverRepository.save(driver);

        return ApiResponse.builder()
                .success(true)
                .message("Driver Added Successfully")
                .data(convertToResponse(driver))
                .build();
    }

    @Override
    public ApiResponse<?> getAllDrivers() {

        List<DriverResponse> drivers = driverRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .success(true)
                .message("Driver List")
                .data(drivers)
                .build();
    }

    @Override
    public ApiResponse<?> getDriverById(Long id) {

        Driver driver = driverRepository.findById(id).orElse(null);

        if (driver == null) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Driver Not Found")
                    .data(null)
                    .build();
        }

        return ApiResponse.builder()
                .success(true)
                .message("Driver Found")
                .data(convertToResponse(driver))
                .build();
    }

    @Override
    public ApiResponse<?> updateDriver(Long id, DriverRequest request) {

        Driver driver = driverRepository.findById(id).orElse(null);

        if (driver == null) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Driver Not Found")
                    .data(null)
                    .build();
        }

        Truck truck = null;

        if (request.getTruckId() != null) {
            truck = truckRepository.findById(request.getTruckId()).orElse(null);
        }

        driver.setFullName(request.getFullName());
        driver.setPhone(request.getPhone());
        driver.setEmail(request.getEmail());
        driver.setLicenseNumber(request.getLicenseNumber());
        driver.setLicenseExpiry(request.getLicenseExpiry());
        driver.setExperience(request.getExperience());
        driver.setSalary(request.getSalary());
        driver.setStatus(request.getStatus());
        driver.setImage(request.getImage());
        driver.setTruck(truck);

        driverRepository.save(driver);

        return ApiResponse.builder()
                .success(true)
                .message("Driver Updated Successfully")
                .data(convertToResponse(driver))
                .build();
    }

    @Override
    public ApiResponse<?> deleteDriver(Long id) {

        Driver driver = driverRepository.findById(id).orElse(null);

        if (driver == null) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Driver Not Found")
                    .data(null)
                    .build();
        }

        driver.setStatus(DriverStatus.INACTIVE);

        driverRepository.save(driver);

        return ApiResponse.builder()
                .success(true)
                .message("Driver Deleted Successfully")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<?> uploadDriverImage(Long driverId, MultipartFile file) {

        Driver driver = driverRepository.findById(driverId).orElse(null);

        if (driver == null) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Driver Not Found")
                    .data(null)
                    .build();
        }

        String imagePath = fileStorageService.uploadDriverImage(file);

        driver.setImage(imagePath);

        driverRepository.save(driver);

        return ApiResponse.builder()
                .success(true)
                .message("Driver Image Uploaded Successfully")
                .data(convertToResponse(driver))
                .build();
    }

    private DriverResponse convertToResponse(Driver driver) {

        return DriverResponse.builder()
                .id(driver.getId())
                .fullName(driver.getFullName())
                .phone(driver.getPhone())
                .email(driver.getEmail())
                .licenseNumber(driver.getLicenseNumber())
                .licenseExpiry(driver.getLicenseExpiry())
                .experience(driver.getExperience())
                .salary(driver.getSalary())
                .status(driver.getStatus())
                .image(driver.getImage())
                .truckId(driver.getTruck() != null ? driver.getTruck().getId() : null)
                .truckNumber(driver.getTruck() != null ? driver.getTruck().getTruckNumber() : null)
                .build();
    }
}