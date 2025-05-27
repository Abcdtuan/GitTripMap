package com.codeWithProject.TripServer.services.admin;

import com.codeWithProject.TripServer.dto.BookingTripDto;
import com.codeWithProject.TripServer.dto.ComboDto;
import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.dto.UserDto;
import com.codeWithProject.TripServer.enums.BookingTripStatus;

import java.io.IOException;
import java.util.List;

public interface AdminService {
    boolean postTrip (TripDto tripDto, List<ComboDto> combos) throws IOException;

    List<TripDto> getAllTrips ();

    void deleteTrip (Long id) ;

    TripDto getTripById (Long id);

    boolean updateTrip(Long tripId, TripDto tripDto) throws IOException;

    List<BookingTripDto> getBookings();

    boolean changeBookingTripStatus(Long bookingId, String status);
    List<UserDto> getAllUsers ();

    boolean deleteCustomer(Long id);

}
