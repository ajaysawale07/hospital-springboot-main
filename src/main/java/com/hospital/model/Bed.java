package com.hospital.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;
import com.hospital.observer.BedStatusObserver;

@Entity
@Table(name = "beds")
public class Bed {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bed_id")
    private int bedId;

    @Column(name = "bed_number", unique = true, nullable = false, length = 20)
    private String bedNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.AVAILABLE;

    @Transient
    private List<BedStatusObserver> observers = new ArrayList<>();

    public enum Status {
        AVAILABLE, OCCUPIED
    }

    // Constructors
    public Bed() {}

    public Bed(String bedNumber, Status status) {
        this.bedNumber = bedNumber;
        this.status = status;
    }

    // Observer pattern methods
    public void addObserver(BedStatusObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(BedStatusObserver observer) {
        observers.remove(observer);
    }

    private void notifyObservers() {
        for (BedStatusObserver observer : observers) {
            observer.update(this.bedNumber, this.status.toString());
        }
    }

    // Modified setter to include observer notification
    public void setStatus(Status status) {
        this.status = status;
        notifyObservers();
    }

    // Other existing getters and setters
    public int getBedId() { return bedId; }
    public String getBedNumber() { return bedNumber; }
    public void setBedNumber(String bedNumber) { this.bedNumber = bedNumber; }
    public Status getStatus() { return status; }
}
