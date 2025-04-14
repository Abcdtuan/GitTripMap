package com.codeWithProject.TripServer.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class TripDto {
    private long id;
    private String name;
    private long price;
    private String tag;
    private long stars;
    private MultipartFile image;
    private String origin;
    private String information;
    private String schedule;
    private String combo;
    private byte[] returnedImage;;
}
