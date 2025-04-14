package com.codeWithProject.TripServer.services.admin;

import com.codeWithProject.TripServer.dto.TripDto;

import java.io.IOException;

public interface AdminService {
    boolean postTrip (TripDto tripDto) throws IOException;
}
