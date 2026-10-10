
package com.truckbooking.service.impl;

import com.truckbooking.dto.request.BookingRequest;
import com.truckbooking.dto.response.BookingResponse;
import com.truckbooking.dto.response.RouteStopResponse;
import com.truckbooking.entity.Booking;
import com.truckbooking.entity.Customer;
import com.truckbooking.entity.Driver;
import com.truckbooking.entity.Route;
import com.truckbooking.entity.Truck;
import com.truckbooking.enums.BookingStatus;
import com.truckbooking.enums.CustomerStatus;
import com.truckbooking.enums.DriverStatus;
import com.truckbooking.enums.TruckAvailability;
import com.truckbooking.enums.TruckStatus;
import com.truckbooking.enums.RouteStatus;
import com.truckbooking.repository.BookingRepository;
import com.truckbooking.repository.CustomerRepository;
import com.truckbooking.repository.DriverRepository;
import com.truckbooking.repository.RouteRepository;
import com.truckbooking.repository.TruckRepository;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.BookingService;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class BookingServiceImpl implements BookingService {

    private final BookingRepository bookingRepository;
    private final CustomerRepository customerRepository;
    private final TruckRepository truckRepository;
    private final DriverRepository driverRepository;
    private final RouteRepository routeRepository;

    private static final Set<BookingStatus> ACTIVE_STATUSES = Set.of(
            BookingStatus.PENDING,
            BookingStatus.CONFIRMED,
            BookingStatus.IN_TRANSIT
    );

    // CREATE BOOKING
    @Override
    public ApiResponse<BookingResponse> addBooking(BookingRequest request) {

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElse(null);

        if (customer == null) {
            return failure("Customer not found");
        }

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            return failure("Inactive customer cannot create a booking");
        }

        if (request.getPickupDate() == null
                || request.getPickupDate().isBefore(LocalDate.now())) {
            return failure("Pickup date cannot be in the past");
        }

        if (request.getPickupLocation().trim()
                .equalsIgnoreCase(request.getDeliveryLocation().trim())) {
            return failure("Pickup and delivery locations must be different");
        }

        Truck truck = truckRepository.findById(request.getTruckId())
                .orElse(null);

        if (truck == null) {
            return failure("Truck not found");
        }

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElse(null);

        if (driver == null) {
            return failure("Driver not found");
        }

        Route route = null;
        if (request.getRouteId() != null) {
            route = routeRepository.findById(request.getRouteId()).orElse(null);
            if (route == null) {
                return failure("Route not found");
            }
            if (route.getStatus() != RouteStatus.ACTIVE) {
                return failure("Inactive route cannot be assigned to a booking");
            }
        }

        String error = validateAssignment(
                truck, driver, request.getCargoWeight(), null
        );

        if (error != null) {
            return failure(error);
        }

        Booking booking = Booking.builder()
                .bookingNumber(generateBookingNumber())
                .customer(customer)
                .pickupLocation(request.getPickupLocation().trim())
                .deliveryLocation(request.getDeliveryLocation().trim())
                .bookingDate(LocalDate.now())
                .pickupDate(request.getPickupDate())
                .cargoDescription(request.getCargoDescription().trim())
                .cargoWeight(request.getCargoWeight())
                .truck(truck)
                .driver(driver)
                .route(route)
                .freightAmount(request.getFreightAmount())
                .status(BookingStatus.PENDING)
                .build();

        Booking savedBooking = bookingRepository.save(booking);

        // Truck availability remains AVAILABLE until confirmation.
        return success("Booking created successfully", toResponse(savedBooking));
    }

    // GET ALL BOOKINGS
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<List<BookingResponse>> getAllBookings() {

        List<BookingResponse> bookings = bookingRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();

        return ApiResponse.<List<BookingResponse>>builder()
                .success(true)
                .message("Bookings fetched successfully")
                .data(bookings)
                .build();
    }

    // GET BOOKING BY ID
    @Override
    @Transactional(readOnly = true)
    public ApiResponse<BookingResponse> getBookingById(Long id) {

        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            return failure("Booking not found");
        }

        return success("Booking fetched successfully", toResponse(booking));
    }

    // UPDATE PENDING BOOKING
    @Override
    public ApiResponse<BookingResponse> updateBooking(
            Long id,
            BookingRequest request
    ) {
        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            return failure("Booking not found");
        }

        if (booking.getStatus() != BookingStatus.PENDING) {
            return failure("Only pending bookings can be updated");
        }

        Customer customer = customerRepository.findById(request.getCustomerId())
                .orElse(null);

        if (customer == null) {
            return failure("Customer not found");
        }

        if (customer.getStatus() != CustomerStatus.ACTIVE) {
            return failure("Inactive customer cannot be assigned");
        }

        if (request.getPickupDate() == null
                || request.getPickupDate().isBefore(LocalDate.now())) {
            return failure("Pickup date cannot be in the past");
        }

        if (request.getPickupLocation().trim()
                .equalsIgnoreCase(request.getDeliveryLocation().trim())) {
            return failure("Pickup and delivery locations must be different");
        }

        Truck truck = truckRepository.findById(request.getTruckId())
                .orElse(null);

        if (truck == null) {
            return failure("Truck not found");
        }

        Driver driver = driverRepository.findById(request.getDriverId())
                .orElse(null);

        if (driver == null) {
            return failure("Driver not found");
        }

        Route route = null;
        if (request.getRouteId() != null) {
            route = routeRepository.findById(request.getRouteId()).orElse(null);
            if (route == null) {
                return failure("Route not found");
            }
            if (route.getStatus() != RouteStatus.ACTIVE) {
                return failure("Inactive route cannot be assigned to a booking");
            }
        }

        String error = validateAssignment(
                truck, driver, request.getCargoWeight(), id
        );

        if (error != null) {
            return failure(error);
        }

        booking.setCustomer(customer);
        booking.setPickupLocation(request.getPickupLocation().trim());
        booking.setDeliveryLocation(request.getDeliveryLocation().trim());
        booking.setPickupDate(request.getPickupDate());
        booking.setCargoDescription(request.getCargoDescription().trim());
        booking.setCargoWeight(request.getCargoWeight());
        booking.setTruck(truck);
        booking.setDriver(driver);
        booking.setRoute(route);
        booking.setFreightAmount(request.getFreightAmount());

        Booking updatedBooking = bookingRepository.save(booking);

        return success("Booking updated successfully", toResponse(updatedBooking));
    }

    // UPDATE BOOKING STATUS
    @Override
    public ApiResponse<BookingResponse> updateBookingStatus(
            Long id,
            BookingStatus newStatus
    ) {
        if (newStatus == null) {
            return failure("Booking status is required");
        }

        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            return failure("Booking not found");
        }

        BookingStatus currentStatus = booking.getStatus();

        if (newStatus == BookingStatus.CANCELLED) {
            return cancelBooking(id);
        }

        // PENDING -> CONFIRMED
        if (currentStatus == BookingStatus.PENDING
                && newStatus == BookingStatus.CONFIRMED) {

            String error = validateAssignment(
                    booking.getTruck(),
                    booking.getDriver(),
                    booking.getCargoWeight(),
                    id
            );

            if (error != null) {
                return failure(error);
            }

            Truck truck = booking.getTruck();

            if (truck.getAvailability() != TruckAvailability.AVAILABLE) {
                return failure("Truck is no longer available");
            }

            truck.setAvailability(TruckAvailability.BOOKED);
            truckRepository.save(truck);

            // CONFIRMED -> IN_TRANSIT
        } else if (currentStatus == BookingStatus.CONFIRMED
                && newStatus == BookingStatus.IN_TRANSIT) {

            // Truck remains BOOKED during transit.

            // IN_TRANSIT -> DELIVERED
        } else if (currentStatus == BookingStatus.IN_TRANSIT
                && newStatus == BookingStatus.DELIVERED) {

            Truck truck = booking.getTruck();
            truck.setAvailability(TruckAvailability.AVAILABLE);
            truckRepository.save(truck);

        } else {
            return failure(
                    "Invalid status transition from "
                            + currentStatus + " to " + newStatus
            );
        }

        booking.setStatus(newStatus);

        Booking updatedBooking = bookingRepository.save(booking);

        return success(
                "Booking status updated successfully",
                toResponse(updatedBooking)
        );
    }

    // CANCEL BOOKING
    @Override
    public ApiResponse<BookingResponse> cancelBooking(Long id) {

        Booking booking = bookingRepository.findById(id).orElse(null);

        if (booking == null) {
            return failure("Booking not found");
        }

        if (booking.getStatus() != BookingStatus.PENDING
                && booking.getStatus() != BookingStatus.CONFIRMED) {
            return failure(
                    "Only pending or confirmed bookings can be cancelled"
            );
        }

        if (booking.getStatus() == BookingStatus.CONFIRMED) {
            Truck truck = booking.getTruck();

            // Release only if no other active booking uses this truck.
            boolean anotherActiveBooking =
                    bookingRepository.existsByTruck_IdAndStatusInAndIdNot(
                            truck.getId(), ACTIVE_STATUSES, id
                    );

            if (!anotherActiveBooking) {
                truck.setAvailability(TruckAvailability.AVAILABLE);
                truckRepository.save(truck);
            }
        }

        booking.setStatus(BookingStatus.CANCELLED);

        Booking cancelledBooking = bookingRepository.save(booking);

        return success("Booking cancelled successfully", toResponse(cancelledBooking));
    }

    // VALIDATE TRUCK AND DRIVER
    private String validateAssignment(
            Truck truck,
            Driver driver,
            Double cargoWeight,
            Long currentBookingId
    ) {
        if (truck == null || driver == null) {
            return "Truck and driver are required";
        }

        if (truck.getStatus() != TruckStatus.ACTIVE) {
            return "Inactive truck cannot be assigned";
        }

        if (truck.getAvailability() != TruckAvailability.AVAILABLE) {
            return "Truck is not available";
        }

        if (driver.getStatus() != DriverStatus.ACTIVE) {
            return "Inactive driver cannot be assigned";
        }

        if (driver.getTruck() == null
                || !driver.getTruck().getId().equals(truck.getId())) {
            return "Driver must be assigned to the selected truck";
        }

        if (cargoWeight == null || cargoWeight <= 0) {
            return "Cargo weight must be greater than zero";
        }

        if (truck.getCapacity() == null
                || cargoWeight > truck.getCapacity()) {
            return "Cargo weight exceeds truck capacity";
        }

        boolean truckConflict;

        if (currentBookingId == null) {
            truckConflict =
                    bookingRepository.existsByTruck_IdAndStatusIn(
                            truck.getId(), ACTIVE_STATUSES
                    );
        } else {
            truckConflict =
                    bookingRepository.existsByTruck_IdAndStatusInAndIdNot(
                            truck.getId(), ACTIVE_STATUSES, currentBookingId
                    );
        }

        if (truckConflict) {
            return "Truck is already assigned to another active booking";
        }

        boolean driverConflict;

        if (currentBookingId == null) {
            driverConflict =
                    bookingRepository.existsByDriver_IdAndStatusIn(
                            driver.getId(), ACTIVE_STATUSES
                    );
        } else {
            driverConflict =
                    bookingRepository.existsByDriver_IdAndStatusInAndIdNot(
                            driver.getId(), ACTIVE_STATUSES, currentBookingId
                    );
        }

        if (driverConflict) {
            return "Driver is already assigned to another active booking";
        }

        return null;
    }

    // GENERATE UNIQUE BOOKING NUMBER
    private String generateBookingNumber() {
        return "BK-" + UUID.randomUUID()
                .toString()
                .substring(0, 8)
                .toUpperCase();
    }

    // ENTITY TO RESPONSE DTO
    private BookingResponse toResponse(Booking booking) {

        Route route = booking.getRoute();

        List<RouteStopResponse> routeStops = route == null
                ? Collections.emptyList()
                : route.getStops().stream()
                .map(stop -> RouteStopResponse.builder()
                        .id(stop.getId())
                        .stopName(stop.getStopName())
                        .stopAddress(stop.getStopAddress())
                        .stopOrder(stop.getStopOrder())
                        .build())
                .toList();

        return BookingResponse.builder()
                .id(booking.getId())
                .bookingNumber(booking.getBookingNumber())

                .customerId(booking.getCustomer().getId())
                .customerName(booking.getCustomer().getFullName())

                .pickupLocation(booking.getPickupLocation())
                .deliveryLocation(booking.getDeliveryLocation())
                .bookingDate(booking.getBookingDate())
                .pickupDate(booking.getPickupDate())

                .cargoDescription(booking.getCargoDescription())
                .cargoWeight(booking.getCargoWeight())

                .truckId(booking.getTruck().getId())
                .truckNumber(booking.getTruck().getTruckNumber())

                .driverId(booking.getDriver().getId())
                .driverName(booking.getDriver().getFullName())

                .routeId(route == null ? null : route.getId())
                .routeCode(route == null ? null : route.getRouteCode())
                .routeName(route == null ? null : route.getRouteName())
                .routeDistanceKm(route == null ? null : route.getDistanceKm())
                .routeEstimatedDurationSeconds(
                        route == null ? null : route.getEstimatedDurationSeconds()
                )
                .routeStops(routeStops)

                .freightAmount(booking.getFreightAmount())
                .status(booking.getStatus())
                .createdAt(booking.getCreatedAt())
                .updatedAt(booking.getUpdatedAt())
                .build();
    }

    // SUCCESS RESPONSE
    private ApiResponse<BookingResponse> success(
            String message,
            BookingResponse data
    ) {
        return ApiResponse.<BookingResponse>builder()
                .success(true)
                .message(message)
                .data(data)
                .build();
    }

    // FAILURE RESPONSE
    private ApiResponse<BookingResponse> failure(String message) {
        return ApiResponse.<BookingResponse>builder()
                .success(false)
                .message(message)
                .data(null)
                .build();
    }
}
