package com.codeWithProject.TripServer.mapper;

import com.codeWithProject.TripServer.dto.FavoriteDto;
import com.codeWithProject.TripServer.dto.FavoriteItemDto;
import com.codeWithProject.TripServer.entity.Favorite;
import com.codeWithProject.TripServer.entity.FavoriteItem;

import java.util.List;
import java.util.stream.Collectors;

public class FavoriteMapper {
    public static FavoriteDto todo(Favorite favorite){

        FavoriteDto dto = new FavoriteDto();
        dto.setId(favorite.getId());
        dto.setUserId(favorite.getUser().getId());
        List<FavoriteItemDto> item = favorite.getItems().stream().map(FavoriteMapper::toItemDto).collect(Collectors.toList());
        dto.setItems(item);
        return dto;
    }

    public static FavoriteItemDto toItemDto(FavoriteItem item){
        FavoriteItemDto dto = new FavoriteItemDto();
        dto.setId(item.getId());
        dto.setTripName(item.getTripName());
        dto.setTripPrice(item.getTripPrice());
        dto.setImage(item.getTripImage());
        return dto;
    }

}
