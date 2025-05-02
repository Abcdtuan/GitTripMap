package com.codeWithProject.TripServer.dto;

import lombok.Data;


@Data
public class ComboOptionDto {
    private long id;
    private String type;
    private Long price;
    private String note;
}
