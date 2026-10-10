
package com.truckbooking.entity;

import com.truckbooking.enums.BookingStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "bookings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String bookingNumber;

    // Customer associated with this booking
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    // Pickup location
    @Column(nullable = false, length = 255)
    private String pickupLocation;

    // Delivery location
    @Column(nullable = false, length = 255)
    private String deliveryLocation;

    // Date on which the booking was created
    @Column(nullable = false)
    private LocalDate bookingDate;

    // Scheduled pickup date
    @Column(nullable = false)
    private LocalDate pickupDate;

    // Description of goods being transported
    @Column(nullable = false, length = 500)
    private String cargoDescription;

    // Cargo weight must use the same unit as Truck.capacity
    @Column(nullable = false)
    private Double cargoWeight;

    // Truck is mandatory for every booking
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "truck_id", nullable = false)
    private Truck truck;

    // Driver is mandatory for every booking
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "driver_id", nullable = false)
    private Driver driver;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "route_id")
    private Route route;


    // Freight charge for this booking
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal freightAmount;

    // Default status for a newly created booking
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private BookingStatus status = BookingStatus.PENDING;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();

        createdAt = now;
        updatedAt = now;

        if (bookingDate == null) {
            bookingDate = LocalDate.now();
        }

        if (status == null) {
            status = BookingStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
