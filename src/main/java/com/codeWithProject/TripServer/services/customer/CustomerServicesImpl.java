package com.codeWithProject.TripServer.services.customer;

import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.entity.Trip;
import com.codeWithProject.TripServer.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerServicesImpl implements CustomerService {
    private final TripRepository tripRepository;

    @Override
    public List<TripDto> getAllTrips() {
        return tripRepository.findAll().stream().map(Trip::getTripDto).collect(Collectors.toList());
    }

    @Override
    public TripDto getTripById(Long id) {
        Optional<Trip> optionalTrip = tripRepository.findById(id);
        return optionalTrip.map(Trip::getTripDto).orElse(null);
    }
}
