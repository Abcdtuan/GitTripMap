package com.codeWithProject.TripServer.dto;

import lombok.Data;

import java.util.List;

@Data
public class FavoriteDto {

    private Long id;
    private Long userId;
    private List<FavoriteItemDto> items;
}
