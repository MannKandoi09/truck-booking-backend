package com.truckbooking.dto.response;

import com.truckbooking.enums.TruckAvailability;
import com.truckbooking.enums.TruckStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class TruckResponse {

    private Long id;

    private String truckNumber;

    private String truckName;

    private String truckType;

    private Double capacity;

    private String model;

    private LocalDate insurance;

    private TruckStatus status;

    private TruckAvailability availability;

    private String image;
}