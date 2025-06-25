package com.hospital.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "doctors")
public class Doctor implements Serializable{

    @Id
    @Column(name = "doctor_id")
    private int doctorId;

    @OneToOne
    @JoinColumn(name = "doctor_id", referencedColumnName = "id", nullable = false, updatable = false)
    private Users user;

    @Column(name = "specialization")
    private String specialization;

    @Column(name = "fee")
    private double fee;

    // Constructors
    public Doctor() {}

    public Doctor(Users user, String specialization, double fee ) {
        this.doctorId = user.getId(); // Ensures doctor_id matches user_id
        this.user = user;
        this.specialization = specialization;
        this.fee = fee;
    }

    // Getters and Setters
    public int getDoctorId() {
        return doctorId;
    }

    public void setDoctorId(int doctorId) {
        this.doctorId = doctorId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
        this.doctorId = user.getId(); // Ensure consistency
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public double getFee() {
        return fee;
    }

    public void setFee(double fee) {
        this.fee = fee;
    }
    @Override
    public String toString() {
        return "Doctors{" +
                "doctorId=" + doctorId +
                ", user=" + user +
                ", specialization='" + specialization + '\'' +
                ", fee=" + fee +
                '}';
    }
    
}
