package com.truckbooking.entity;

import com.truckbooking.enums.TruckAvailability;
import com.truckbooking.enums.TruckStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "trucks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Truck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "truck_number", nullable = false, unique = true)
    private String truckNumber;

    @Column(name = "truck_name", nullable = false)
    private String truckName;

    @Column(name = "truck_type", nullable = false)
    private String truckType;

    @Column(nullable = false)
    private Double capacity;

    @Column(nullable = false)
    private String model;

    @Column(nullable = false)
    private LocalDate insurance;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruckStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TruckAvailability availability;

    private String image;

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