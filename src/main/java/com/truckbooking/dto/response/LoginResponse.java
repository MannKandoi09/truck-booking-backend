package com.truckbooking.dto.response;

import com.truckbooking.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginResponse {

    private String token;
    private String type;

    private Long id;
    private String firstName;
    private String lastName;
    private String email;

    private Role role;

}