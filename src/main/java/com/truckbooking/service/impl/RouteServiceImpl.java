
package com.truckbooking.service.impl;

import com.truckbooking.dto.route.RouteRequest;
import com.truckbooking.dto.route.RouteResponse;
import com.truckbooking.dto.route.RouteStopRequest;
import com.truckbooking.dto.response.RouteStopResponse;
import com.truckbooking.entity.Route;
import com.truckbooking.entity.RouteStop;
import com.truckbooking.enums.RouteStatus;
import com.truckbooking.exception.BadRequestException;
import com.truckbooking.exception.ResourceNotFoundException;
import com.truckbooking.repository.RouteRepository;
import com.truckbooking.service.RouteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class RouteServiceImpl implements RouteService {

    private final RouteRepository routeRepository;

    @Override
    public RouteResponse createRoute(RouteRequest request) {
        validateRouteRequest(request);

        String routeCode = request.getRouteCode().trim();

        if (routeRepository.existsByRouteCodeIgnoreCase(routeCode)) {
            throw new BadRequestException("Route code already exists");
        }

        Route route = Route.builder()
                .routeCode(routeCode)
                .routeName(request.getRouteName().trim())
                .source(request.getSource().trim())
                .destination(request.getDestination().trim())
                .distanceKm(request.getDistanceKm())
                .estimatedDurationSeconds(
                        request.getEstimatedDurationSeconds())
                .status(request.getStatus() == null
                        ? RouteStatus.ACTIVE
                        : request.getStatus())
                .build();

        addStops(route, request.getStops());

        Route savedRoute = routeRepository.save(route);

        return toResponse(savedRoute);
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteResponse> getAllRoutes() {
        return routeRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public List<RouteResponse> getActiveRoutes() {
        return routeRepository
                .findByStatusOrderByRouteNameAsc(RouteStatus.ACTIVE)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public RouteResponse getRouteById(Long id) {
        return toResponse(findRoute(id));
    }

    @Override
    public RouteResponse updateRoute(Long id, RouteRequest request) {
        validateRouteRequest(request);

        Route route = findRoute(id);
        String routeCode = request.getRouteCode().trim();

        routeRepository.findByRouteCodeIgnoreCase(routeCode)
                .ifPresent(existing -> {
                    if (!existing.getId().equals(id)) {
                        throw new BadRequestException(
                                "Route code already exists");
                    }
                });

        route.setRouteCode(routeCode);
        route.setRouteName(request.getRouteName().trim());
        route.setSource(request.getSource().trim());
        route.setDestination(request.getDestination().trim());
        route.setDistanceKm(request.getDistanceKm());
        route.setEstimatedDurationSeconds(
                request.getEstimatedDurationSeconds());

        if (request.getStatus() != null) {
            route.setStatus(request.getStatus());
        }

        /*
         * Remove old stops and flush deletions before inserting
         * replacements. This prevents the unique constraint on
         * (route_id, stop_order) from failing during an update.
         */
        for (RouteStop stop : new ArrayList<>(route.getStops())) {
            route.removeStop(stop);
        }

        routeRepository.flush();

        addStops(route, request.getStops());

        Route updatedRoute = routeRepository.save(route);

        return toResponse(updatedRoute);
    }

    @Override
    public RouteResponse updateRouteStatus(Long id, RouteStatus status) {
        if (status == null) {
            throw new BadRequestException("Route status is required");
        }

        Route route = findRoute(id);
        route.setStatus(status);

        return toResponse(routeRepository.save(route));
    }

    private Route findRoute(Long id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "Route not found with id: " + id));
    }

    private void validateRouteRequest(RouteRequest request) {
        if (request == null) {
            throw new BadRequestException("Route request is required");
        }

        if (request.getRouteCode() == null
                || request.getRouteCode().isBlank()) {
            throw new BadRequestException("Route code is required");
        }

        if (request.getRouteName() == null
                || request.getRouteName().isBlank()) {
            throw new BadRequestException("Route name is required");
        }

        if (request.getSource() == null
                || request.getSource().isBlank()) {
            throw new BadRequestException("Source is required");
        }

        if (request.getDestination() == null
                || request.getDestination().isBlank()) {
            throw new BadRequestException("Destination is required");
        }

        if (request.getDistanceKm() == null
                || !Double.isFinite(request.getDistanceKm())
                || request.getDistanceKm() <= 0) {
            throw new BadRequestException(
                    "Distance must be a positive number");
        }

        if (request.getEstimatedDurationSeconds() == null
                || request.getEstimatedDurationSeconds() <= 0) {
            throw new BadRequestException(
                    "Estimated duration must be greater than zero");
        }

        if (request.getStops() == null) {
            return;
        }

        List<Integer> orders = new ArrayList<>();

        for (RouteStopRequest stop : request.getStops()) {
            if (stop == null
                    || stop.getStopName() == null
                    || stop.getStopName().isBlank()
                    || stop.getStopAddress() == null
                    || stop.getStopAddress().isBlank()
                    || stop.getStopOrder() == null
                    || stop.getStopOrder() <= 0) {
                throw new BadRequestException(
                        "Each stop needs a name, address and positive order");
            }

            if (orders.contains(stop.getStopOrder())) {
                throw new BadRequestException(
                        "Duplicate stop order: " + stop.getStopOrder());
            }

            orders.add(stop.getStopOrder());
        }
    }

    private void addStops(
            Route route,
            List<RouteStopRequest> stopRequests) {

        if (stopRequests == null || stopRequests.isEmpty()) {
            return;
        }

        List<RouteStopRequest> sortedStops = stopRequests.stream()
                .sorted(Comparator.comparing(
                        RouteStopRequest::getStopOrder))
                .toList();

        for (RouteStopRequest stopRequest : sortedStops) {
            RouteStop stop = RouteStop.builder()
                    .stopName(stopRequest.getStopName().trim())
                    .stopAddress(stopRequest.getStopAddress().trim())
                    .stopOrder(stopRequest.getStopOrder())
                    .build();

            route.addStop(stop);
        }
    }

    private RouteResponse toResponse(Route route) {
        List<RouteStopResponse> stops = route.getStops()
                .stream()
                .sorted(Comparator.comparing(RouteStop::getStopOrder))
                .map(stop -> RouteStopResponse.builder()
                        .id(stop.getId())
                        .stopName(stop.getStopName())
                        .stopAddress(stop.getStopAddress())
                        .stopOrder(stop.getStopOrder())
                        .build())
                .toList();

        return RouteResponse.builder()
                .id(route.getId())
                .routeCode(route.getRouteCode())
                .routeName(route.getRouteName())
                .source(route.getSource())
                .destination(route.getDestination())
                .distanceKm(route.getDistanceKm())
                .estimatedDurationSeconds(
                        route.getEstimatedDurationSeconds())
                .status(route.getStatus())
                .stops(stops)
                .createdAt(route.getCreatedAt())
                .updatedAt(route.getUpdatedAt())
                .build();
    }
}
