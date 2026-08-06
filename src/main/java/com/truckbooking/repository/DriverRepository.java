package com.truckbooking.repository;

import com.truckbooking.entity.Driver;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DriverRepository extends JpaRepository<Driver, Long> {

    boolean existsByLicenseNumber(String licenseNumber);

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<Driver> findByLicenseNumber(String licenseNumber);
}