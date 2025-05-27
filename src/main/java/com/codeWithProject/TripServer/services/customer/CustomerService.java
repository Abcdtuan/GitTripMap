package com.codeWithProject.TripServer.services.customer;

import com.codeWithProject.TripServer.dto.BookingTripDto;
import com.codeWithProject.TripServer.dto.FavoriteItemDto;
import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.dto.UserDto;

import java.util.List;
import java.util.Optional;

public interface CustomerService {

    List<TripDto> getAllTrips ();

    TripDto getTripById (Long id);

    boolean bookingTrip(BookingTripDto bookingTripDto);

    List<BookingTripDto> getBookingByUserId(Long userId);
    UserDto addNewUser(UserDto userDto);

    void deleteCustomer(Long id);

    UserDto getCurrentUserProfile(String email);

    Optional<UserDto> updateCustomer(String email, UserDto userDto);

    boolean isExistingTrip (Long id);

    void addTripToFavourite (Long userId, Long tripId);

    void removeTripFromFavorite(Long favoriteId);

    List<FavoriteItemDto> getFavoriteItemsByUserId(Long userId);

}
