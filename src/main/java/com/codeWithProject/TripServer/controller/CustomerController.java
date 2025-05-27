package com.codeWithProject.TripServer.controller;

import com.codeWithProject.TripServer.dto.BookingTripDto;
import com.codeWithProject.TripServer.dto.FavoriteItemDto;
import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.dto.UserDto;
import com.codeWithProject.TripServer.services.customer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping ("api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/trips")
    public ResponseEntity<List<TripDto>> getAllTrips(){
        List<TripDto> tripDtoList = customerService.getAllTrips();
        return ResponseEntity.ok(tripDtoList);
    }

    @GetMapping("/trip/{id}")
    public ResponseEntity<TripDto>getTripById(@PathVariable Long id) {
        TripDto tripDto = customerService.getTripById(id);
        return ResponseEntity.ok(tripDto);
    }

    @PostMapping("/trip/booking")
    public ResponseEntity<Void> bookingTrip(@RequestBody BookingTripDto bookingTripDto) {

        boolean success = customerService.bookingTrip(bookingTripDto);
        if (success) {
            return ResponseEntity.status(HttpStatus.CREATED).build();
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();

    }

    @GetMapping("/trip/bookings/{userId}")
    public ResponseEntity<List<BookingTripDto>> getBookingByTripId(@PathVariable Long userId) {
        return ResponseEntity.ok(customerService.getBookingByUserId(userId));
    }
    @GetMapping("/profile")
    public ResponseEntity<UserDto> getProfile(@AuthenticationPrincipal UserDetails userDetails) {
        String email = userDetails.getUsername(); // hoặc lấy id nếu bạn dùng custom UserDetails
        UserDto userDto =  customerService.getCurrentUserProfile(email);
        return ResponseEntity.ok(userDto);
    }
    @PutMapping("/updateCustomer")
    public ResponseEntity<?> updateCustomer(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserDto userDto) {
        String email = userDetails.getUsername();
        return customerService.updateCustomer(email,userDto)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.status(HttpStatus.NOT_FOUND).body((UserDto) Map.of("message", "Không tìm thấy khách hàng")));
    }
    @DeleteMapping("/deleteCustomer/{id}")
    public ResponseEntity<?> deleteCustomer(@PathVariable Long id) {
        if(!customerService.isExistingTrip(id)){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        customerService.deleteCustomer(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
    @GetMapping("/favorite/{userId}")
    public ResponseEntity<List<FavoriteItemDto>> getFavoriteItemsByUser(@PathVariable Long userId) {
        List<FavoriteItemDto> favoriteItems = customerService.getFavoriteItemsByUserId(userId);
        return ResponseEntity.ok(favoriteItems);
    }

    // Thêm chuyến đi vào favorite của user
    @PostMapping("/favorite/add/{userId}/{tripId}")
    public ResponseEntity<?> addTripToFavorite(@PathVariable Long userId, @PathVariable Long tripId) {
        try {
            customerService.addTripToFavourite(userId, tripId);
            return ResponseEntity.ok(Map.of("message", "Added to favorite successfully"));
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
        }
    }

    // Xóa chuyến đi khỏi favorite của user
    @DeleteMapping("/favorite/remove/{favoriteId}")
    public ResponseEntity<?> removeFromFavorite(@PathVariable Long favoriteId) {
        try {
            customerService.removeTripFromFavorite(favoriteId);
            return ResponseEntity.ok(Map.of("message", "Favorite item removed successfully"));
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", e.getMessage()));
        }
    }
}
