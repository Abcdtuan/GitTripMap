package com.codeWithProject.TripServer.dto;

import lombok.Data;

import java.util.List;

@Data
public class ComboDto {
    private long id;
    private String name;
    private long price;
    private String description;
    private List<ComboOptionDto> options;

}
