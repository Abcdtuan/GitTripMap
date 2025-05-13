package com.codeWithProject.TripServer.dto;

import com.codeWithProject.TripServer.enums.BookingTripStatus;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingTripDto {
    private long id;



    private LocalDate bookingDate;

    private Long price;

    private int numberOfPeople;

    private String bookingTripStatus;

    private Long tripId;

    private Long userId;

    private Long comboId;

    private String name;

    private String email;

    private String tripName;

    private String comboName;

}
