package com.codeWithProject.TripServer.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Data
public class TripDto {
    private long id;
    private String name;
    private long price;
    private String tag;
    private MultipartFile image;
    private String origin;
    private String information;
    private String schedule;
    private String combos;
    private byte[] returnedImage;;
}
