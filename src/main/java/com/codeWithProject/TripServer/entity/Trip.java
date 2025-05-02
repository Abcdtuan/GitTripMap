package com.codeWithProject.TripServer.entity;

import com.codeWithProject.TripServer.dto.ComboDto;
import com.codeWithProject.TripServer.dto.ComboOptionDto;
import com.codeWithProject.TripServer.dto.TripDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.persistence.*;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Data
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;

    private long price;

    @Lob
    @Column(columnDefinition = "Text")
    private String tag;



    @Lob
    @Column(columnDefinition = "Text")
    private String origin;

    @Lob
    @Column(columnDefinition = "Text")
    private String information;

    @Lob
    @Column(columnDefinition = "Text")
    private String schedule;

    @OneToMany(mappedBy = "trip", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Combo> combos = new ArrayList<>();

    @Lob
    @Column(columnDefinition = "longblob")
    private byte[] image;

    public TripDto getTripDto() {
        TripDto tripDto = new TripDto();
        tripDto.setId(id);
        tripDto.setName(name);
        tripDto.setPrice(price);
        tripDto.setTag(tag);
        tripDto.setOrigin(origin);
        tripDto.setInformation(information);
        tripDto.setSchedule(schedule);
        tripDto.setReturnedImage(image);

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            String combosJson = objectMapper.writeValueAsString(
                    combos.stream().map(combo -> {
                        ComboDto dto = new ComboDto();
                        dto.setName(combo.getName());
                        dto.setPrice(combo.getPrice());
                        dto.setDescription(combo.getDescription());
                        dto.setOptions(combo.getOptions().stream().map(opt -> {
                            ComboOptionDto optDto = new ComboOptionDto();
                            optDto.setType(opt.getType());
                            optDto.setPrice(opt.getPrice());
                            optDto.setNote(opt.getNote());
                            return optDto;
                        }).collect(Collectors.toList()));
                        return dto;
                    }).collect(Collectors.toList())
            );
            tripDto.setCombos(combosJson);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            tripDto.setCombos("[]"); // Nếu lỗi thì set danh sách rỗng
        }


        return tripDto;
    }


}
