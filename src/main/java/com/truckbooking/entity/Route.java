
package com.truckbooking.entity;

import com.truckbooking.enums.RouteStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(
        name = "routes",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_route_code",
                        columnNames = "route_code"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Route {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "route_code", nullable = false, unique = true, length = 30)
    private String routeCode;

    @Column(name = "route_name", nullable = false, length = 100)
    private String routeName;

    @Column(nullable = false, length = 255)
    private String source;

    @Column(nullable = false, length = 255)
    private String destination;

    @Column(name = "distance_km")
    private Double distanceKm;

    @Column(name = "estimated_duration_seconds")
    private Long estimatedDurationSeconds;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private RouteStatus status = RouteStatus.ACTIVE;

    @OneToMany(
            mappedBy = "route",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @OrderBy("stopOrder ASC")
    @Builder.Default
    private List<RouteStop> stops = new ArrayList<>();

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist() {
        LocalDateTime now = LocalDateTime.now();
        createdAt = now;
        updatedAt = now;

        if (status == null) {
            status = RouteStatus.ACTIVE;
        }
    }

    @PreUpdate
    public void preUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public void addStop(RouteStop stop) {
        stops.add(stop);
        stop.setRoute(this);
    }

    public void removeStop(RouteStop stop) {
        stops.remove(stop);
        stop.setRoute(null);
    }
}
