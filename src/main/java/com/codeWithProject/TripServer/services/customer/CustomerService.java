package com.codeWithProject.TripServer.services.customer;

import com.codeWithProject.TripServer.dto.TripDto;

import java.util.List;

public interface CustomerService {

    List<TripDto> getAllTrips ();

    TripDto getTripById (Long id);
}
