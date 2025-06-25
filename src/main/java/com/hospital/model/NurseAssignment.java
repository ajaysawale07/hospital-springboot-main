package com.hospital.model;

import jakarta.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "nurse_assignments")
public class NurseAssignment implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "nurse_id", nullable = false, updatable = false)
    private int nurseId;

    @Column(name = "patient_id", nullable = false, updatable = false)
    private int patientId;

//    @ManyToOne
//    @JoinColumn(name = "nurse_id", referencedColumnName = "nurse_id", nullable = false)
//    private Nurse nurse;
//
//    @ManyToOne
//    @JoinColumn(name = "patient_id", referencedColumnName = "patient_id", nullable = false)
//    private Patient patient;

    // Constructors
    public NurseAssignment() {}

    public NurseAssignment(Nurse nurse, Patient patient) {
//        this.nurse = nurse;
//        this.patient = patient;
        this.nurseId = nurse.getNurseId();
        this.patientId = patient.getPatientId();
    }

    public NurseAssignment(int nurse_id, int patient_id) {
        this.nurseId = nurse_id;
        this.patientId = patient_id;
    }

    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

//    public Nurse getNurse() {
//        return nurse;
//    }
//
//    public void setNurse(Nurse nurse) {
//        this.nurse = nurse;
//    }
//
//    public Patient getPatient() {
//        return patient;
//    }
//
//    public void setPatient(Patient patient) {
//        this.patient = patient;
//    }

    public int getNurseId() {
        return nurseId;
    }

    public void setNurseId(int nurseId) {
        this.nurseId = nurseId;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }
}
