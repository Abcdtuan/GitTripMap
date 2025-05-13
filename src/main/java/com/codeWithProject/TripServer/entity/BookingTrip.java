package com.codeWithProject.TripServer.entity;


import com.codeWithProject.TripServer.dto.BookingTripDto;
import com.codeWithProject.TripServer.enums.BookingTripStatus;
import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;

@Entity
@Data
public class BookingTrip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private LocalDate bookingDate;

    private Long price;

    @Enumerated(EnumType.STRING)
    private BookingTripStatus bookingTripStatus;

    private int numberOfPeople;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "trip_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Trip trip;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "combo_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Combo combo;

    public BookingTripDto getBookingTripDto() {
        BookingTripDto bookingTripDto = new BookingTripDto();
        bookingTripDto.setId(id);
        bookingTripDto.setBookingDate(bookingDate);
        bookingTripDto.setPrice(price);
        bookingTripDto.setNumberOfPeople(numberOfPeople);
        bookingTripDto.setBookingTripStatus(bookingTripStatus.toString());
        bookingTripDto.setName(user.getName());
        bookingTripDto.setEmail(user.getEmail());
        bookingTripDto.setTripName(trip.getName());
        bookingTripDto.setComboName(combo.getName());
        bookingTripDto.setUserId(user.getId());
        bookingTripDto.setTripId(trip.getId());
        bookingTripDto.setComboId(combo.getId());

        return bookingTripDto;
    }
}
