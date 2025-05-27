package com.codeWithProject.TripServer.dto;

import com.codeWithProject.TripServer.enums.UserRole;
import lombok.Data;

@Data
public class AuthenticationResponse {

    private String jwt;
    private UserRole userRole;
    private Long userId;
    private String name;




}
