package com.codeWithProject.TripServer.services.customer;


import com.codeWithProject.TripServer.dto.BookingTripDto;
import com.codeWithProject.TripServer.dto.ComboDto;
import com.codeWithProject.TripServer.dto.ComboOptionDto;
import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.entity.*;
import com.codeWithProject.TripServer.enums.BookingTripStatus;
import com.codeWithProject.TripServer.repository.BookingTripRepository;
import com.codeWithProject.TripServer.repository.ComboRepository;
import com.codeWithProject.TripServer.repository.TripRepository;
import com.codeWithProject.TripServer.repository.UserRepository;
import com.codeWithProject.TripServer.utils.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServicesImpl implements CustomerService {
    private final TripRepository tripRepository;

    private final UserRepository userRepository;
    private final ComboRepository comboRepository;

    private final BookingTripRepository bookingTripRepository;

    @Override
    public List<TripDto> getAllTrips() {
        return tripRepository.findAll().stream().map(Trip::getTripDto).collect(Collectors.toList());
    }


    @Override
    public TripDto getTripById(Long id) {
        Optional<Trip> optionalTrip = tripRepository.findById(id);
        if (optionalTrip.isPresent()) {
            Trip trip = optionalTrip.get();

            // Map từ Trip sang TripDto
            TripDto tripDto = new TripDto();
            tripDto.setId(trip.getId());
            tripDto.setName(trip.getName());
            tripDto.setPrice(trip.getPrice());
            tripDto.setTag(trip.getTag());
            tripDto.setOrigin(trip.getOrigin());
            tripDto.setInformation(trip.getInformation());
            tripDto.setSchedule(trip.getSchedule());
            tripDto.setReturnedImage(trip.getImage());

            // Chuyển danh sách combos thành chuỗi JSON
            ObjectMapper objectMapper = new ObjectMapper();
            try {
                String combosJson = objectMapper.writeValueAsString(
                        trip.getCombos().stream().map(combo -> {
                            ComboDto comboDto = new ComboDto();
                            comboDto.setId(combo.getId()); // Đảm bảo comboId được ánh xạ đúng
                            comboDto.setName(combo.getName());
                            comboDto.setPrice(combo.getPrice());
                            comboDto.setDescription(combo.getDescription());
                            comboDto.setOptions(combo.getOptions().stream().map(option -> {
                                ComboOptionDto optionDto = new ComboOptionDto();
                                optionDto.setId(option.getId());
                                optionDto.setType(option.getType());
                                optionDto.setPrice(option.getPrice());
                                optionDto.setNote(option.getNote());
                                return optionDto;
                            }).collect(Collectors.toList()));
                            return comboDto;
                        }).collect(Collectors.toList())
                );
                tripDto.setCombos(combosJson); // Gán chuỗi JSON vào combos
            } catch (JsonProcessingException e) {
                e.printStackTrace();
                tripDto.setCombos("[]"); // Nếu lỗi, gán danh sách rỗng
            }

            return tripDto;
        }

        throw new RuntimeException("Trip not found with id: " + id);
    }

    @Override
    public boolean bookingTrip(BookingTripDto bookingTripDto) {
        Optional<Trip> optionalTrip = tripRepository.findById(bookingTripDto.getTripId());
        Optional<User> optionalUser = userRepository.findById(bookingTripDto.getUserId());
        Optional<Combo> optionalCombo = comboRepository.findById(bookingTripDto.getComboId());
        if (optionalTrip.isPresent() && optionalUser.isPresent() && optionalCombo.isPresent()) {
            Trip existingTrip = optionalTrip.get();
            User existingUser = optionalUser.get();
            Combo existingCombo = optionalCombo.get();
            BookingTrip bookingTrip = new BookingTrip();
            bookingTrip.setUser(existingUser);
            bookingTrip.setTrip(existingTrip);
            bookingTrip.setCombo(existingCombo);
            bookingTrip.setBookingTripStatus(BookingTripStatus.PENDING);
            bookingTrip.setBookingDate(bookingTripDto.getBookingDate());
            bookingTrip.setNumberOfPeople(bookingTripDto.getNumberOfPeople());
            bookingTrip.setPrice(existingCombo.getPrice() * bookingTripDto.getNumberOfPeople());

            bookingTripRepository.save(bookingTrip);
            return true;

        }
        return false;
    }

    @Override
    public List<BookingTripDto> getBookingByUserId(Long userId) {
        return bookingTripRepository.findAllByUserId(userId).stream().map(BookingTrip::getBookingTripDto).collect(Collectors.toList());
    }


}
