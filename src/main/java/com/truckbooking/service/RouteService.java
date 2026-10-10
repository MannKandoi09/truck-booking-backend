
package com.truckbooking.service;

import com.truckbooking.dto.route.RouteRequest;
import com.truckbooking.dto.route.RouteResponse;
import com.truckbooking.enums.RouteStatus;

import java.util.List;

public interface RouteService {

    RouteResponse createRoute(RouteRequest request);

    List<RouteResponse> getAllRoutes();

    List<RouteResponse> getActiveRoutes();

    RouteResponse getRouteById(Long id);

    RouteResponse updateRoute(Long id, RouteRequest request);

    RouteResponse updateRouteStatus(Long id, RouteStatus status);
}
