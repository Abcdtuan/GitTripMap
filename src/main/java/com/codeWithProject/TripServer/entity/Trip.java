package com.codeWithProject.TripServer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "trips")
public class Trip {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private String name;
    private long price;
    private String tag;
    private long stars;
    private String origin;
    private String information;
    private String schedule;
    private String combo;
    @Column(columnDefinition = "longblob")
    private byte[] image;

}
