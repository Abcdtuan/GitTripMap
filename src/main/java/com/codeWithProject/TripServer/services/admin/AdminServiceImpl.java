package com.codeWithProject.TripServer.services.admin;

import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.entity.Trip;
import com.codeWithProject.TripServer.repository.TripRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final TripRepository tripRepository;

    @Override
    public boolean postTrip(TripDto tripDto) throws IOException {
        try{
            Trip trip = new Trip();
            trip.setName(tripDto.getName());
            trip.setPrice(tripDto.getPrice());
            trip.setTag(tripDto.getTag());
            trip.setStars(tripDto.getStars());
            trip.setOrigin(tripDto.getOrigin());
            trip.setInformation(tripDto.getInformation());
            trip.setSchedule(tripDto.getSchedule());
            trip.setCombo(tripDto.getCombo());
            trip.setImage(tripDto.getImage().getBytes());
            tripRepository.save(trip);
            return true;
        }catch(Exception e){
            return false;
        }


    }
}
