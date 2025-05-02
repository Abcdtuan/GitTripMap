package com.codeWithProject.TripServer.controller;

import com.codeWithProject.TripServer.dto.ComboDto;
import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.repository.TripRepository;
import com.codeWithProject.TripServer.services.admin.AdminService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {
    private final AdminService adminService;
    private final ObjectMapper objectMapper;
    
    @PostMapping("/trip")
    public ResponseEntity<?> postTrip(@ModelAttribute TripDto tripDto) throws IOException {
        try {
            List<ComboDto> combos = objectMapper.readValue(
                    tripDto.getCombos(),
                    new TypeReference<List<ComboDto>>() {}
            );

            boolean success = adminService.postTrip(tripDto, combos);
            if (success) {
                return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "Thêm chuyến đi thành công"));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("message", "Thêm chuyến đi thất bại"));
            }
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Dữ liệu gửi lên không hợp lệ", "details", e.getMessage()));
        }
    }
    @GetMapping("/trips")
    public ResponseEntity<?> getAllTrips() {
        return ResponseEntity.ok(adminService.getAllTrips());
    }
    @DeleteMapping("/trip/{id}")
    public ResponseEntity<?> deleteTrip (@PathVariable Long id){
        adminService.deleteTrip(id);
        return ResponseEntity.ok(null);
    }

    @GetMapping("/trip/{id}")
    public ResponseEntity<TripDto>getTripById(@PathVariable Long id) {
        TripDto tripDto = adminService.getTripById(id);
        return ResponseEntity.ok(tripDto);
    }

    @PutMapping("/trip/{tripId}")
    public ResponseEntity<Void> updateTrip(@PathVariable Long tripId, @ModelAttribute TripDto tripDto) throws IOException {

        try{
            boolean success = adminService.updateTrip(tripId, tripDto);
            if (success) {
                return ResponseEntity.status(HttpStatus.OK).build();
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

}
