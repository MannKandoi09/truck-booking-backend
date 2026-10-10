
package com.truckbooking.dto.route;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStopRequest {

    @NotBlank(message = "Stop name is required")
    @Size(max = 100, message = "Stop name cannot exceed 100 characters")
    private String stopName;

    @NotBlank(message = "Stop address is required")
    @Size(max = 255, message = "Stop address cannot exceed 255 characters")
    private String stopAddress;

    @NotNull(message = "Stop order is required")
    @Positive(message = "Stop order must be greater than zero")
    private Integer stopOrder;
}
