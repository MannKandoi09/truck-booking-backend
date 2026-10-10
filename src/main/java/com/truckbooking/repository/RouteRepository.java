
package com.truckbooking.repository;

import com.truckbooking.entity.Route;
import com.truckbooking.enums.RouteStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface RouteRepository extends JpaRepository<Route, Long> {

    boolean existsByRouteCodeIgnoreCase(String routeCode);

    Optional<Route> findByRouteCodeIgnoreCase(String routeCode);

    List<Route> findByStatusOrderByRouteNameAsc(RouteStatus status);
}
