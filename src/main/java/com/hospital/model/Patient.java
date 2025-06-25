package com.hospital.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "patients")
public class Patient implements Serializable {

    @Id
    @Column(name = "patient_id")
    private int patientId;

    @OneToOne
    @MapsId
    @JoinColumn(name = "patient_id", referencedColumnName = "id")
    private Users user;

    @Column(name = "admission_date")
    @Temporal(TemporalType.DATE)
    private Date admissionDate;

    @Column(name = "discharge_date")
    @Temporal(TemporalType.DATE)
    private Date dischargeDate;


    // Constructors
    public Patient() {}

    public Patient(Users user, Date admissionDate, int patientId) {
        this.patientId = patientId;
        this.user = user;
        this.admissionDate = admissionDate;
    }

    // Getters and Setters
    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
        this.patientId = user.getId(); // Ensure consistency
    }

    public int getId(){
        return patientId;
    }

    public Date getAdmissionDate() {
        return admissionDate;
    }

    public void setAdmissionDate(Date admissionDate) {
        this.admissionDate = admissionDate;
    }

    public Date getDischargeDate() {
        return dischargeDate;
    }

    public void setDischargeDate(Date dischargeDate) {
        this.dischargeDate = dischargeDate;
    }

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", user=" + user.getUsername() +  // Showing username instead of full user object
                ", admissionDate=" + admissionDate +
                ", dischargeDate=" + dischargeDate +
                '}';
    }
}
