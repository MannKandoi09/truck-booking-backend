
package com.truckbooking.dto.response;

import com.truckbooking.enums.BookingStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookingResponse {

    private Long id;
    private String bookingNumber;

    private Long customerId;
    private String customerName;

    private String pickupLocation;
    private String deliveryLocation;
    private LocalDate bookingDate;
    private LocalDate pickupDate;

    private String cargoDescription;
    private Double cargoWeight;

    private Long truckId;
    private String truckNumber;

    private Long driverId;
    private String driverName;

    // Route details
    private Long routeId;
    private String routeCode;
    private String routeName;
    private Double routeDistanceKm;
    private Long routeEstimatedDurationSeconds;
    private List<RouteStopResponse> routeStops;

    private BigDecimal freightAmount;
    private BookingStatus status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
