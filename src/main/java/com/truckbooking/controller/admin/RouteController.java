
package com.truckbooking.controller.admin;

import com.truckbooking.dto.route.RouteRequest;
import com.truckbooking.dto.route.RouteResponse;
import com.truckbooking.enums.RouteStatus;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.RouteService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/routes")
@RequiredArgsConstructor
public class RouteController {

    private final RouteService routeService;

    @PostMapping
    public ResponseEntity<ApiResponse<RouteResponse>> createRoute(
            @Valid @RequestBody RouteRequest request) {

        RouteResponse response = routeService.createRoute(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.<RouteResponse>builder()
                        .success(true)
                        .message("Route created successfully")
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<RouteResponse>>> getAllRoutes() {
        List<RouteResponse> routes = routeService.getAllRoutes();

        return ResponseEntity.ok(
                ApiResponse.<List<RouteResponse>>builder()
                        .success(true)
                        .message("Routes fetched successfully")
                        .data(routes)
                        .build());
    }

    @GetMapping("/active")
    public ResponseEntity<ApiResponse<List<RouteResponse>>> getActiveRoutes() {
        List<RouteResponse> routes = routeService.getActiveRoutes();

        return ResponseEntity.ok(
                ApiResponse.<List<RouteResponse>>builder()
                        .success(true)
                        .message("Active routes fetched successfully")
                        .data(routes)
                        .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteResponse>> getRouteById(
            @PathVariable Long id) {

        RouteResponse response = routeService.getRouteById(id);

        return ResponseEntity.ok(
                ApiResponse.<RouteResponse>builder()
                        .success(true)
                        .message("Route fetched successfully")
                        .data(response)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<RouteResponse>> updateRoute(
            @PathVariable Long id,
            @Valid @RequestBody RouteRequest request) {

        RouteResponse response = routeService.updateRoute(id, request);

        return ResponseEntity.ok(
                ApiResponse.<RouteResponse>builder()
                        .success(true)
                        .message("Route updated successfully")
                        .data(response)
                        .build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<ApiResponse<RouteResponse>> updateRouteStatus(
            @PathVariable Long id,
            @RequestParam RouteStatus status) {

        RouteResponse response =
                routeService.updateRouteStatus(id, status);

        return ResponseEntity.ok(
                ApiResponse.<RouteResponse>builder()
                        .success(true)
                        .message("Route status updated successfully")
                        .data(response)
                        .build());
    }
}
