package com.truckbooking.service.impl;

import com.truckbooking.dto.request.TruckRequest;
import com.truckbooking.dto.response.TruckResponse;
import com.truckbooking.entity.Truck;
import com.truckbooking.enums.TruckStatus;
import com.truckbooking.repository.TruckRepository;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.FileStorageService;
import com.truckbooking.service.TruckService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TruckServiceImpl implements TruckService {

    private final TruckRepository truckRepository;
    private final FileStorageService fileStorageService;

    @Override
    public ApiResponse<?> addTruck(TruckRequest request) {

        if (truckRepository.existsByTruckNumber(request.getTruckNumber())) {

            return ApiResponse.builder()
                    .success(false)
                    .message("Truck Number already exists")
                    .data(null)
                    .build();
        }

        Truck truck = Truck.builder()
                .truckNumber(request.getTruckNumber())
                .truckName(request.getTruckName())
                .truckType(request.getTruckType())
                .capacity(request.getCapacity())
                .model(request.getModel())
                .insurance(request.getInsurance())
                .status(request.getStatus())
                .availability(request.getAvailability())
                .image(request.getImage())
                .build();

        truckRepository.save(truck);

        return ApiResponse.builder()
                .success(true)
                .message("Truck Added Successfully")
                .data(convertToResponse(truck))
                .build();
    }

    @Override
    public ApiResponse<?> getAllTrucks() {

        List<TruckResponse> trucks = truckRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());

        return ApiResponse.builder()
                .success(true)
                .message("Truck List")
                .data(trucks)
                .build();
    }

    @Override
    public ApiResponse<?> getTruckById(Long id) {

        Truck truck = truckRepository.findById(id)
                .orElse(null);

        if (truck == null) {

            return ApiResponse.builder()
                    .success(false)
                    .message("Truck Not Found")
                    .data(null)
                    .build();
        }

        return ApiResponse.builder()
                .success(true)
                .message("Truck Found")
                .data(convertToResponse(truck))
                .build();
    }

    @Override
    public ApiResponse<?> updateTruck(Long id, TruckRequest request) {

        Truck truck = truckRepository.findById(id)
                .orElse(null);

        if (truck == null) {

            return ApiResponse.builder()
                    .success(false)
                    .message("Truck Not Found")
                    .data(null)
                    .build();
        }

        truck.setTruckName(request.getTruckName());
        truck.setTruckType(request.getTruckType());
        truck.setCapacity(request.getCapacity());
        truck.setModel(request.getModel());
        truck.setInsurance(request.getInsurance());
        truck.setStatus(request.getStatus());
        truck.setAvailability(request.getAvailability());

        truckRepository.save(truck);

        return ApiResponse.builder()
                .success(true)
                .message("Truck Updated Successfully")
                .data(convertToResponse(truck))
                .build();
    }

    @Override
    public ApiResponse<?> deleteTruck(Long id) {

        Truck truck = truckRepository.findById(id)
                .orElse(null);

        if (truck == null) {

            return ApiResponse.builder()
                    .success(false)
                    .message("Truck Not Found")
                    .data(null)
                    .build();
        }

        truck.setStatus(TruckStatus.INACTIVE);

        truckRepository.save(truck);

        return ApiResponse.builder()
                .success(true)
                .message("Truck Deleted Successfully")
                .data(null)
                .build();
    }

    @Override
    public ApiResponse<?> uploadTruckImage(Long truckId, MultipartFile file) {

        Truck truck = truckRepository.findById(truckId)
                .orElse(null);

        if (truck == null) {
            return ApiResponse.builder()
                    .success(false)
                    .message("Truck Not Found")
                    .data(null)
                    .build();
        }

        String imagePath = fileStorageService.uploadTruckImage(file);

        truck.setImage(imagePath);

        truckRepository.save(truck);

        return ApiResponse.builder()
                .success(true)
                .message("Truck Image Uploaded Successfully")
                .data(convertToResponse(truck))
                .build();
    }

    private TruckResponse convertToResponse(Truck truck) {

        return TruckResponse.builder()
                .id(truck.getId())
                .truckNumber(truck.getTruckNumber())
                .truckName(truck.getTruckName())
                .truckType(truck.getTruckType())
                .capacity(truck.getCapacity())
                .model(truck.getModel())
                .insurance(truck.getInsurance())
                .status(truck.getStatus())
                .availability(truck.getAvailability())
                .image(truck.getImage())
                .build();
    }
}