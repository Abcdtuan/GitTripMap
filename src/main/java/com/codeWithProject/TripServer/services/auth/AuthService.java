package com.codeWithProject.TripServer.services.auth;

import com.codeWithProject.TripServer.dto.SignupRequest;
import com.codeWithProject.TripServer.dto.UserDto;

public interface AuthService {

    UserDto createCustomer(SignupRequest signupRequest);

    boolean hasCustomerWithEmail(String email);
}
