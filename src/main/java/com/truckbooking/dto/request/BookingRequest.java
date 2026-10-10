
package com.truckbooking.dto.request;

import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDate;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingRequest {

    @NotNull(message = "Customer ID is required")
    @Positive(message = "Customer ID must be positive")
    private Long customerId;

    @NotBlank(message = "Pickup location is required")
    @Size(max = 255)
    private String pickupLocation;

    @NotBlank(message = "Delivery location is required")
    @Size(max = 255)
    private String deliveryLocation;

    @NotNull(message = "Pickup date is required")
    @FutureOrPresent(message = "Pickup date cannot be in the past")
    private LocalDate pickupDate;

    @NotBlank(message = "Cargo description is required")
    @Size(max = 500)
    private String cargoDescription;

    @NotNull(message = "Cargo weight is required")
    @Positive(message = "Cargo weight must be greater than zero")
    private Double cargoWeight;

    // Optional: truck can be assigned later.
    private Long truckId;

    // Optional: driver can be assigned later.
    private Long driverId;

    // Optional: route can be assigned during booking.
    @Positive(message = "Route ID must be positive")
    private Long routeId;

    @NotNull(message = "Freight amount is required")
    @DecimalMin(
            value = "0.01",
            message = "Freight amount must be greater than zero"
    )
    private BigDecimal freightAmount;
}
