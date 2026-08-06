package com.truckbooking.dto.request;

import com.truckbooking.enums.DriverStatus;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDate;

@Data
public class DriverRequest {

    @NotBlank
    private String fullName;

    @NotBlank
    @Size(min = 10, max = 10)
    private String phone;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String licenseNumber;

    @NotNull
    private LocalDate licenseExpiry;

    @NotNull
    private Integer experience;

    @NotNull
    private Double salary;

    @NotNull
    private DriverStatus status;

    private String image;

    private Long truckId;
}