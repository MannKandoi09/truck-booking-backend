package com.truckbooking.repository;

import com.truckbooking.entity.Truck;
import com.truckbooking.enums.TruckAvailability;
import com.truckbooking.enums.TruckStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TruckRepository extends JpaRepository<Truck, Long> {

    // Check Truck Number
    boolean existsByTruckNumber(String truckNumber);

    // Find By Truck Number
    Optional<Truck> findByTruckNumber(String truckNumber);

    // Filter By Status
    List<Truck> findByStatus(TruckStatus status);

    // Filter By Availability
    List<Truck> findByAvailability(TruckAvailability availability);

    // Pagination + Sorting
    Page<Truck> findAll(Pageable pageable);

}