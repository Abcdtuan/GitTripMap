package com.codeWithProject.TripServer.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "combooptions")
public class ComboOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String type;
    private Long price;
    private String note;

    @ManyToOne
    @JoinColumn(name = "combo_id")
    @JsonIgnore
        private Combo combo;

}
