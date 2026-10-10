
package com.truckbooking.dto.route;

import com.truckbooking.dto.response.RouteStopResponse;
import com.truckbooking.enums.RouteStatus;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteResponse {

    private Long id;
    private String routeCode;
    private String routeName;
    private String source;
    private String destination;
    private Double distanceKm;
    private Long estimatedDurationSeconds;
    private RouteStatus status;
    private List<RouteStopResponse> stops;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
