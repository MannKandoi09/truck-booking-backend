
package com.truckbooking.repository;

import com.truckbooking.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {

    boolean existsByPhone(String phone);

    boolean existsByEmail(String email);

    Optional<Customer> findByPhone(String phone);

    Optional<Customer> findByEmail(String email);
}

