package com.truckbooking.entity;

import com.truckbooking.enums.DriverStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "drivers")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Driver {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Driver Name
    @Column(nullable = false)
    private String fullName;

    // Mobile Number
    @Column(nullable = false, unique = true, length = 10)
    private String phone;

    // Email
    @Column(nullable = false, unique = true)
    private String email;

    // License Number
    @Column(nullable = false, unique = true)
    private String licenseNumber;

    // License Expiry Date
    @Column(nullable = false)
    private LocalDate licenseExpiry;

    // Experience
    @Column(nullable = false)
    private Integer experience;

    // Salary
    @Column(nullable = false)
    private Double salary;

    // Driver Status
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DriverStatus status;

    // Driver Image
    private String image;

    // Assigned Truck
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "truck_id")
    private Truck truck;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }
}