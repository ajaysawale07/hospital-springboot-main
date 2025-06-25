package com.hospital.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nurses")
public class Nurse implements Serializable {

    @Id
    @Column(name = "nurse_id")
    private int nurseId;

    @OneToOne
    @JoinColumn(name = "nurse_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Users user;

    @Column(name = "current_patient_count", nullable = false)
    private int currentPatientCount = 0;

    // Constructors
    public Nurse() {}

    public Nurse(Users user, int currentPatientCount) {
        this.nurseId = user.getId();
        this.user = user;
        setCurrentPatientCount(currentPatientCount); // Ensure constraint is maintained
    }
    public Nurse(int nurseId) {
        this.nurseId = nurseId;
        this.currentPatientCount = 0; // Default value
    }

    // Getters and Setters
    public int getNurseId() {
        return nurseId;
    }

    public void setNurseId(int nurseId) {
        this.nurseId = nurseId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
        this.nurseId = user.getId();
    }

    public int getCurrentPatientCount() {
        return currentPatientCount;
    }

    public void setCurrentPatientCount(int currentPatientCount) {
        if (currentPatientCount <= 10) {
            this.currentPatientCount = currentPatientCount;
        } else {
            throw new IllegalArgumentException("Current patient count cannot exceed 10");
        }
    }

    @Override
    public String toString() {
        return "Nurse{" +
                "nurseId=" + nurseId +
                ", user=" + user.getUsername() +
                ", currentPatientCount=" + currentPatientCount +
                '}';
    }
}
