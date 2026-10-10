
package com.truckbooking.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "route_stops",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_route_stop_order",
                        columnNames = {"route_id", "stop_order"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RouteStop {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id", nullable = false)
    private Route route;

    @Column(name = "stop_name", nullable = false, length = 100)
    private String stopName;

    @Column(name = "stop_address", nullable = false, length = 255)
    private String stopAddress;

    @Column(name = "stop_order", nullable = false)
    private Integer stopOrder;
}
