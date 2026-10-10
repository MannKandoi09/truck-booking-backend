
package com.truckbooking.repository;

import com.truckbooking.entity.Booking;
import com.truckbooking.enums.BookingStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;

public interface BookingRepository extends JpaRepository<Booking, Long> {

    boolean existsByBookingNumber(String bookingNumber);

    boolean existsByTruck_IdAndStatusIn(
            Long truckId,
            Collection<BookingStatus> statuses
    );

    boolean existsByDriver_IdAndStatusIn(
            Long driverId,
            Collection<BookingStatus> statuses
    );

    boolean existsByTruck_IdAndStatusInAndIdNot(
            Long truckId,
            Collection<BookingStatus> statuses,
            Long id
    );

    boolean existsByDriver_IdAndStatusInAndIdNot(
            Long driverId,
            Collection<BookingStatus> statuses,
            Long id
    );

}
