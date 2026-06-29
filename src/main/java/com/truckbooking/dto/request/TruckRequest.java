package com.truckbooking.dto.request;

import com.truckbooking.enums.TruckAvailability;
import com.truckbooking.enums.TruckStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TruckRequest {

    @NotBlank(message = "Truck Number is required")
    private String truckNumber;

    @NotBlank(message = "Truck Name is required")
    private String truckName;

    @NotBlank(message = "Truck Type is required")
    private String truckType;

    @NotNull(message = "Capacity is required")
    private Double capacity;

    @NotBlank(message = "Model is required")
    private String model;

    @NotNull(message = "Insurance date is required")
    private LocalDate insurance;

    @NotNull(message = "Status is required")
    private TruckStatus status;

    @NotNull(message = "Availability is required")
    private TruckAvailability availability;

    private String image;
}