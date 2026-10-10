
package com.truckbooking.dto.route;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteRequest {

    @NotBlank(message = "Route code is required")
    @Size(max = 30, message = "Route code cannot exceed 30 characters")
    private String routeCode;

    @NotBlank(message = "Route name is required")
    @Size(max = 100, message = "Route name cannot exceed 100 characters")
    private String routeName;

    @NotBlank(message = "Source is required")
    @Size(max = 255, message = "Source cannot exceed 255 characters")
    private String source;

    @NotBlank(message = "Destination is required")
    @Size(max = 255, message = "Destination cannot exceed 255 characters")
    private String destination;


    @NotNull(message = "Distance is required")
    @jakarta.validation.constraints.Positive(message = "Distance must be greater than zero")
    private Double distanceKm;

    @NotNull(message = "Estimated duration is required")
    @jakarta.validation.constraints.Positive(message = "Duration must be greater than zero")
    private Long estimatedDurationSeconds;


    @NotNull(message = "Route status is required")
    @Builder.Default
    private com.truckbooking.enums.RouteStatus status =
            com.truckbooking.enums.RouteStatus.ACTIVE;

    @Valid
    @Builder.Default
    private List<RouteStopRequest> stops = new ArrayList<>();
}
