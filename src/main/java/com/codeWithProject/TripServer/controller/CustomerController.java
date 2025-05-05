package com.codeWithProject.TripServer.controller;

import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.repository.TripRepository;
import com.codeWithProject.TripServer.services.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
}
