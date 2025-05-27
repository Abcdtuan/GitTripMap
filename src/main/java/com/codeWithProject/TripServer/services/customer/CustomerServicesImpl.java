package com.codeWithProject.TripServer.services.customer;


import com.codeWithProject.TripServer.dto.*;
import com.codeWithProject.TripServer.entity.*;
import com.codeWithProject.TripServer.enums.BookingTripStatus;
import com.codeWithProject.TripServer.mapper.FavoriteMapper;
import com.codeWithProject.TripServer.repository.*;
import com.codeWithProject.TripServer.utils.JWTUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    private final BookingTripRepository bookingTripRepository;
    private final FavoriteRepository favoriteRepository;
    private final FavoriteItemRepository favoriteItemRepository;

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
    @Override
    public UserDto addNewUser(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        User savedUser = userRepository.save(user);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto getCurrentUserProfile(String email) {
        User user = userRepository.findFirstByEmail(email).orElseThrow(()-> new RuntimeException("User not found"));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public boolean isExistingTrip(Long id) {
        return userRepository.existsById(id);
    }

    @Override
    public Optional<UserDto> updateCustomer(String email, UserDto userDto) {
        return userRepository.findByEmail(email).map(exitingCustomer ->{
            Optional.ofNullable(userDto.getName()).ifPresent(exitingCustomer::setName);
            Optional.ofNullable(userDto.getEmail()).ifPresent(exitingCustomer::setEmail);
            User updateUser = userRepository.save(exitingCustomer);
            UserDto updatedUserDto = modelMapper.map(updateUser, UserDto.class);
            return updatedUserDto;
        });
    }

    @Override
    public void deleteCustomer(Long id) {
        userRepository.deleteById(id);
    }

    @Override
    public void addTripToFavourite(Long userId, Long tripId) {
        User user = userRepository.findById(userId).orElseThrow(()-> new RuntimeException("User not found"));
        Trip trip = tripRepository.findById(tripId).orElseThrow(() -> new RuntimeException("Trip not found"));

        Favorite favorite = favoriteRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Favorite newFav = new Favorite();
                    newFav.setUser(user); // hoặc: userRepository.findById(userId).get()
                    return favoriteRepository.save(newFav);
                });
        // Kiểm tra xem chuyến đi đã có trong danh sách yêu thích chưa
        boolean alreadyExists = favorite.getItems().stream()
                .anyMatch(item -> item.getTrip() != null && item.getTrip().getId() == tripId);
        if (alreadyExists) {
            throw new RuntimeException("Trip already in favourite");
        }
        FavoriteItem item = new FavoriteItem();
        item.setTrip(trip);
        item.setTripName(trip.getName());
        item.setTripPrice(trip.getPrice());
        item.setTripImage(trip.getImage());
        item.setFavorite(favorite);
        favorite.getItems().add(item);

        favoriteRepository.save(favorite);

    }
    @Override
    public void removeTripFromFavorite(Long favoriteId) {
        // Tìm mục yêu thích dựa trên favoriteId
        FavoriteItem favoriteItem = favoriteItemRepository.findById(favoriteId)
                .orElseThrow(() -> new RuntimeException("Favorite item not found for favoriteId: " + favoriteId));

        // Xóa mục yêu thích
        favoriteItemRepository.delete(favoriteItem);
    }

    @Override
    public List<FavoriteItemDto> getFavoriteItemsByUserId(Long userId) {
        Favorite favorite = favoriteRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Favorite not found for userId: " + userId));

        return favorite.getItems().stream()
                .map(FavoriteMapper::toItemDto)
                .collect(Collectors.toList());
    }

}
