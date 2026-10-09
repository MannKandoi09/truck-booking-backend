
package com.truckbooking.dto.response;

import com.truckbooking.enums.CustomerStatus;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerResponse {

    private Long id;
    private String fullName;
    private String phone;
    private String email;
    private String companyName;
    private String address;
    private String city;
    private String state;
    private String pincode;
    private CustomerStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}

