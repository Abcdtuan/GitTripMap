package com.codeWithProject.TripServer.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class FavoriteItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Favorite favorite;

    @ManyToOne
    private Trip trip;

    private String tripName;

    private Long tripPrice;

    @Lob
    @Column( columnDefinition = "LONGBLOB")
    private byte[] tripImage;

}
