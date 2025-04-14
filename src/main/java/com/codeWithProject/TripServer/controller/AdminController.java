package com.codeWithProject.TripServer.controller;

import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.repository.TripRepository;
import com.codeWithProject.TripServer.services.admin.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final TripRepository tripRepository;

    @PostMapping("/trip")
    public ResponseEntity<?> postTrip(@ModelAttribute TripDto tripDto) throws IOException {
         boolean success =  adminService.postTrip(tripDto);
         if(success){
             return ResponseEntity.status(HttpStatus.CREATED).build();
         }else
             return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
    }

}
