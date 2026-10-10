
package com.truckbooking.service;

import com.truckbooking.dto.request.BookingRequest;
import com.truckbooking.dto.response.BookingResponse;
import com.truckbooking.enums.BookingStatus;
import com.truckbooking.response.ApiResponse;

import java.util.List;

public interface BookingService {

    ApiResponse<BookingResponse> addBooking(BookingRequest request);

    ApiResponse<List<BookingResponse>> getAllBookings();

    ApiResponse<BookingResponse> getBookingById(Long id);

    ApiResponse<BookingResponse> updateBooking(
            Long id,
            BookingRequest request
    );

    ApiResponse<BookingResponse> updateBookingStatus(
            Long id,
            BookingStatus status
    );

    ApiResponse<BookingResponse> cancelBooking(Long id);
}
