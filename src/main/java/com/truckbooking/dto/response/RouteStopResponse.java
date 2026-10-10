
package com.truckbooking.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStopResponse {

    private Long id;
    private String stopName;
    private String stopAddress;
    private Integer stopOrder;
}
