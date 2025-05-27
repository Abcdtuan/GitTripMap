package com.codeWithProject.TripServer.dto;

import lombok.Data;

@Data
public class FavoriteItemDto {
    private Long id;
    private String tripName;
    private double tripPrice;
    private byte[] image;

}
