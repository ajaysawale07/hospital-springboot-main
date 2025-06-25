package com.hospital.model;

import jakarta.persistence.*;

@Entity
@Table(name = "patient_tests")
public class PatientTest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne
    @JoinColumn(name = "patient_id", nullable = false)
    private Patient patient;

    @ManyToOne
    @JoinColumn(name = "test_id", nullable = false)
    private Test test;

    // Constructors
    public PatientTest() {}

    public PatientTest(Patient patient, Test test) {
        this.patient = patient;
        this.test = test;
    }

    // Getters and Setters
    public int getId() { return id; }
    public Patient getPatient() { return patient; }
    public void setPatient(Patient patient) { this.patient = patient; }
    public Test getTest() { return test; }
    public void setTest(Test test) { this.test = test; }
}
