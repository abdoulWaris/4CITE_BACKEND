package com.akkor.hotel.dto;

import com.akkor.hotel.model.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;
    private Long userId;
    private String email;
    private String firstName;
    private String lastName;
    private Set<Role> roles;
} 