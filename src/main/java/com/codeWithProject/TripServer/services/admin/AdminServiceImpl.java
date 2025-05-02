package com.codeWithProject.TripServer.services.admin;

import com.codeWithProject.TripServer.dto.ComboDto;
import com.codeWithProject.TripServer.dto.TripDto;
import com.codeWithProject.TripServer.entity.Combo;
import com.codeWithProject.TripServer.entity.ComboOption;
import com.codeWithProject.TripServer.entity.Trip;
import com.codeWithProject.TripServer.repository.TripRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {
    private final TripRepository tripRepository;

    @Override
    public boolean postTrip(TripDto tripDto, List<ComboDto> combos) throws IOException {
        try{
            Trip trip = new Trip();
            trip.setName(tripDto.getName());
            trip.setPrice(tripDto.getPrice());
            trip.setTag(tripDto.getTag());
            trip.setOrigin(tripDto.getOrigin());
            trip.setInformation(tripDto.getInformation());
            trip.setSchedule(tripDto.getSchedule());
            trip.setImage(tripDto.getImage().getBytes());

            List<Combo> comboList = combos.stream().map(dto ->{
                Combo combo = new Combo();
                combo.setName(dto.getName());
                combo.setDescription(dto.getDescription());
                combo.setPrice(dto.getPrice());
                combo.setTrip(trip);
                List<ComboOption> options = dto.getOptions().stream().map(optDto ->{
                    ComboOption option = new ComboOption();
                    option.setType(optDto.getType());
                    option.setPrice(optDto.getPrice());
                    option.setNote(optDto.getNote());
                    option.setCombo(combo);
                    return option;
                }).toList();
                combo.setOptions(options);
                return combo;
            }).toList();
            trip.setCombos(comboList);
            tripRepository.save(trip);
            return true;
        }catch(Exception e){
            System.out.println("Lỗi: " + e.getMessage());
            return false;
        }


    }

    @Override
    public List<TripDto> getAllTrips() {
        return tripRepository.findAll().stream().map(Trip::getTripDto).collect(Collectors.toList());
    }

    @Override
    public void deleteTrip(Long id) {
        tripRepository.deleteById(id);

    }

    @Override
    public TripDto getTripById(Long id) {
        Optional<Trip> optionalTrip = tripRepository.findById(id);
        return optionalTrip.map(Trip::getTripDto).orElse(null);
    }

    @Override
    public boolean updateTrip(Long tripId, TripDto tripDto) throws IOException {
        Optional<Trip> optionalTrip = tripRepository.findById(tripId);
        if (optionalTrip.isPresent()) {
            Trip existingTrip = optionalTrip.get();
            if(tripDto.getImage() != null){
                existingTrip.setImage(tripDto.getImage().getBytes());
            }
            existingTrip.setName(tripDto.getName());
            existingTrip.setPrice(tripDto.getPrice());
            existingTrip.setTag(tripDto.getTag());
            existingTrip.setOrigin(tripDto.getOrigin());
            existingTrip.setInformation(tripDto.getInformation());
            existingTrip.setSchedule(tripDto.getSchedule());
            existingTrip.setImage(tripDto.getImage().getBytes());
            if (tripDto.getCombos() != null && !tripDto.getCombos().isEmpty()) {
                ObjectMapper objectMapper = new ObjectMapper();
                List<ComboDto> comboDtoList = objectMapper.readValue(
                        tripDto.getCombos(),
                        new TypeReference<List<ComboDto>>() {}
                );

                List<Combo> newCombos = comboDtoList.stream().map(dto -> {
                    Combo combo = new Combo();
                    combo.setName(dto.getName());
                    combo.setDescription(dto.getDescription());
                    combo.setPrice(dto.getPrice());
                    combo.setTrip(existingTrip);

                    if (dto.getOptions() != null) {
                        List<ComboOption> options = dto.getOptions().stream().map(optDto -> {
                            ComboOption option = new ComboOption();
                            option.setType(optDto.getType());
                            option.setPrice(optDto.getPrice());
                            option.setNote(optDto.getNote());
                            option.setCombo(combo);
                            return option;
                        }).toList();
                        combo.setOptions(options);
                    }

                    return combo;
                }).toList();

                existingTrip.getCombos().addAll(newCombos);
            }

            tripRepository.save(existingTrip);
            return true;
        }
        return false;
    }
}
