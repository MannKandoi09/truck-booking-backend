
package com.truckbooking.controller.admin;

import com.truckbooking.dto.request.BookingRequest;
import com.truckbooking.dto.response.BookingResponse;
import com.truckbooking.enums.BookingStatus;
import com.truckbooking.response.ApiResponse;
import com.truckbooking.service.BookingService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin/bookings")
@RequiredArgsConstructor
public class BookingController {

    private final BookingService bookingService;

    // Create booking
    @PostMapping
    public ApiResponse<BookingResponse> addBooking(
            @Valid @RequestBody BookingRequest request
    ) {
        return bookingService.addBooking(request);
    }

    // Get all bookings
    @GetMapping
    public ApiResponse<List<BookingResponse>> getAllBookings() {
        return bookingService.getAllBookings();
    }

    // Get booking by ID
    @GetMapping("/{id}")
    public ApiResponse<BookingResponse> getBookingById(
            @PathVariable Long id
    ) {
        return bookingService.getBookingById(id);
    }

    // Update pending booking
    @PutMapping("/{id}")
    public ApiResponse<BookingResponse> updateBooking(
            @PathVariable Long id,
            @Valid @RequestBody BookingRequest request
    ) {
        return bookingService.updateBooking(id, request);
    }

    // Update booking status
    @PatchMapping("/{id}/status")
    public ApiResponse<BookingResponse> updateBookingStatus(
            @PathVariable Long id,
            @RequestParam BookingStatus status
    ) {
        return bookingService.updateBookingStatus(id, status);
    }

    // Cancel booking
    @PatchMapping("/{id}/cancel")
    public ApiResponse<BookingResponse> cancelBooking(
            @PathVariable Long id
    ) {
        return bookingService.cancelBooking(id);
    }
}
