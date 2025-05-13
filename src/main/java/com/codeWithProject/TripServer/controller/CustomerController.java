package com.codeWithProject.TripServer.controller;

import com.codeWithProject.TripServer.dto.BookingTripDto;
import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.services.customer.CustomerService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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

    
}
