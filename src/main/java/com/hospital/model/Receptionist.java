package com.hospital.model;

import jakarta.persistence.*;

@Entity
@Table(name = "receptionists")
public class Receptionist {

    @Id
    @Column(name = "receptionist_id")
    private int receptionistId;



    // Constructors
    public Receptionist() {}

    public Receptionist(int receptionistId) {
        this.receptionistId = receptionistId;
    }

    // Getters and Setters
}
