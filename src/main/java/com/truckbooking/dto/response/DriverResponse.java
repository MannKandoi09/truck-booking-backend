package com.truckbooking.dto.response;

import com.truckbooking.enums.DriverStatus;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class DriverResponse {

    private Long id;

    private String fullName;

    private String phone;

    private String email;

    private String licenseNumber;

    private LocalDate licenseExpiry;

    private Integer experience;

    private Double salary;

    private DriverStatus status;

    private String image;

    private Long truckId;

    private String truckNumber;
}